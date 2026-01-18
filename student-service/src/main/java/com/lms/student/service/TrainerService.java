package com.lms.student.service;

import com.lms.student.dto.CreateTrainerRequest;
import com.lms.student.dto.TrainerResponse;
import com.lms.student.kafka.TrainerEventProducer;
import com.lms.student.model.Trainer;
import com.lms.student.repo.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TrainerService {

    private final TrainerRepository repo;
    private final TrainerEventProducer producer;

    public TrainerService(TrainerRepository repo,
                          TrainerEventProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    public TrainerResponse create(CreateTrainerRequest req) {

        if (repo.existsByEmail(req.getEmail())) {
            throw new IllegalStateException("Trainer already exists");
        }

        Trainer t = new Trainer();
        t.setName(req.getName());
        t.setEmail(req.getEmail());
        t.setExpertise(req.getExpertise());
        t.setStatus("ACTIVE");

        Trainer saved = repo.save(t);

        producer.send("TRAINER_CREATED", Map.of(
                "trainerId", saved.getId(),
                "email", saved.getEmail()
        ));

        return toResponse(saved);
    }

    public List<TrainerResponse> list() {
        return repo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TrainerResponse updateStatus(Long id, String status) {
        Trainer t = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        t.setStatus(status);
        Trainer saved = repo.save(t);

        producer.send("TRAINER_STATUS_CHANGED", Map.of(
                "trainerId", id,
                "status", status
        ));

        return toResponse(saved);
    }

    public void delete(Long id) {
        repo.deleteById(id);

        producer.send("TRAINER_DELETED", Map.of(
                "trainerId", id
        ));
    }

    private TrainerResponse toResponse(Trainer t) {
        TrainerResponse r = new TrainerResponse();
        r.setId(t.getId());
        r.setName(t.getName());
        r.setEmail(t.getEmail());
        r.setExpertise(t.getExpertise());
        r.setStatus(t.getStatus());
        r.setCreatedAt(t.getCreatedAt()); // 🔥 FIXED
        return r;
    }
}
