package com.lms.user.service;

import com.lms.user.dto.CreateUserRequest;
import com.lms.user.dto.UpdateUserRequest;
import com.lms.user.dto.UserEvent;
import com.lms.user.dto.UserResponse;
import com.lms.user.exception.ResourceNotFoundException;
import com.lms.user.kafka.UserEventProducer;
import com.lms.user.model.User;
import com.lms.user.repo.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;
    private final UserEventProducer producer;
    private final RestTemplate restTemplate = new RestTemplate();

    // 🔑 AUTH SERVICE URL (DIRECT — NO GATEWAY, NO JWT)
    private static final String AUTH_SERVICE_URL =
            "http://localhost:8081/api/auth/register";

    public UserService(UserRepository repo, UserEventProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    // ----------------------------------------------------
    // CREATE USER (ADMIN FLOW)
    // ----------------------------------------------------
    @CacheEvict(value = "users", allEntries = true)
    public UserResponse createUser(CreateUserRequest req) {

        Optional<User> exists = repo.findByEmail(req.getEmail());
        if (exists.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // 1️⃣ CREATE AUTH CREDENTIALS
        Map<String, Object> authPayload = new HashMap<>();
        authPayload.put("name", req.getDisplayName());
        authPayload.put("email", req.getEmail());
        authPayload.put("password", req.getPassword());
        authPayload.put(
                "role",
                req.getRoles() != null
                        ? req.getRoles().replace("ROLE_", "")
                        : "STUDENT"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(authPayload, headers);

        try {
            restTemplate.postForEntity(
                    AUTH_SERVICE_URL,
                    entity,
                    Void.class
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create auth credentials",
                    e
            );
        }

        // 2️⃣ SAVE USER PROFILE
        User u = new User();
        u.setEmail(req.getEmail());
        u.setDisplayName(req.getDisplayName());
        u.setTenantId(req.getTenantId());
        u.setRoles(req.getRoles());

        User saved = repo.save(u);

        // 3️⃣ PUBLISH EVENT (NULL SAFE)
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("id", saved.getId());
        eventData.put("email", saved.getEmail());
        eventData.put(
                "displayName",
                saved.getDisplayName() != null
                        ? saved.getDisplayName()
                        : saved.getEmail()
        );

        producer.send(new UserEvent("USER_CREATED", eventData));

        return mapToResponse(saved);
    }

    // ----------------------------------------------------
    // GET USER BY ID
    // ----------------------------------------------------
    @Cacheable(value = "users", key = "#id")
    public UserResponse getById(Long id) {
        return repo.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + id
                        ));
    }

    // ----------------------------------------------------
    // UPDATE USER
    // ----------------------------------------------------
    @CacheEvict(value = "users", key = "#id")
    public UserResponse updateUser(Long id, UpdateUserRequest req) {

        User u = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id " + id
                        ));

        if (req.getDisplayName() != null) {
            u.setDisplayName(req.getDisplayName());
        }

        if (req.getRoles() != null) {
            u.setRoles(req.getRoles());
        }

        User saved = repo.save(u);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("id", saved.getId());
        eventData.put("email", saved.getEmail());
        eventData.put(
                "displayName",
                saved.getDisplayName() != null
                        ? saved.getDisplayName()
                        : saved.getEmail()
        );

        producer.send(new UserEvent("USER_UPDATED", eventData));

        return mapToResponse(saved);
    }

    // ----------------------------------------------------
    // DELETE USER
    // ----------------------------------------------------
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {

        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException(
                    "User not found with id " + id
            );
        }

        repo.deleteById(id);

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("id", id);

        producer.send(new UserEvent("USER_DELETED", eventData));
    }

    // ----------------------------------------------------
    // LIST USERS
    // ----------------------------------------------------
    // ✅ ONLY CHANGE: CACHE REMOVED (MISSING FIX)
    public Page<UserResponse> listUsers(
            int page,
            int size,
            String sort,
            String dir
    ) {

        Sort.Direction direction =
                "desc".equalsIgnoreCase(dir)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Sort s = Sort.by(
                direction,
                (sort == null || sort.isEmpty()) ? "id" : sort
        );

        Pageable p = PageRequest.of(page, size, s);

        return repo.findAll(p).map(this::mapToResponse);
    }

    // ----------------------------------------------------
    // MAP ENTITY → DTO
    // ----------------------------------------------------
    private UserResponse mapToResponse(User u) {
        UserResponse r = new UserResponse();
        r.setId(u.getId());
        r.setEmail(u.getEmail());
        r.setDisplayName(u.getDisplayName());
        r.setRoles(u.getRoles());
        r.setTenantId(u.getTenantId());
        r.setCreatedAt(u.getCreatedAt());
        return r;
    }
}
