package com.lms.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lms.auth.dto.AuthResponse;
import com.lms.auth.dto.RegisterRequest;
import com.lms.auth.model.Role;
import com.lms.auth.model.User;
import com.lms.auth.repository.UserRepository;
import com.lms.auth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    @Value("${google.client-id}")
    private String googleClientId;

    private final ConcurrentHashMap<String, String> resetTokens = new ConcurrentHashMap<>();

    // ✅ Google-recommended reusable components
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static NetHttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    // ================= REGISTER =================
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already registered"
            );
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.STUDENT);

        userRepository.save(user);
    }

    // ================= EMAIL LOGIN =================
    public AuthResponse authenticate(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        String token = jwtUtil.generateToken(user);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }

    // ================= GOOGLE LOGIN =================
    public AuthResponse authenticateGoogle(String  idToken, Role role) {
     System.out.println(idToken);
     System.out.println(role);
        if (idToken == null || idToken.isBlank()) {
            throw new RuntimeException("Google ID token is missing");
        }

        try {
            GoogleIdTokenVerifier verifier =
                    new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
                            .setAudience(Collections.singletonList(googleClientId))
                            .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                throw new RuntimeException("Invalid Google token");
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> createGoogleUser(email, name, role));
            
            String jwt = jwtUtil.generateToken(user);

            return new AuthResponse(
                    jwt,
                    user.getEmail(),
                    user.getRole().name()
            );

        } 
        catch (Exception e) {
            e.printStackTrace();   // 🔥 THIS IS CRITICAL
            throw new RuntimeException(e);
        }

    }

    // ================= FORGOT PASSWORD =================
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        String token = UUID.randomUUID().toString();
        resetTokens.put(token, email);

        String resetLink =
                "http://localhost:5173/reset-password?token=" + token;

        emailService.sendResetPasswordMail(email, resetLink);
    }

    // ================= RESET PASSWORD =================
    public void resetPassword(String token, String newPassword) {

        String email = resetTokens.get(token);
        if (email == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid or expired token"
            );
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetTokens.remove(token);
    }

    // ================= GOOGLE USER CREATION =================
    private User createGoogleUser(String email, String name, Role role) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);

        user.setPassword(
                passwordEncoder.encode(UUID.randomUUID().toString())
        );

        user.setRole(role != null ? role : Role.STUDENT);

        return userRepository.save(user);
    }
}
