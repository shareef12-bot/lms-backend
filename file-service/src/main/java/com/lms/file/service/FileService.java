package com.lms.file.service;

import com.lms.file.kafka.FileEventProducer;
import com.lms.file.model.FileResource;
import com.lms.file.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.time.Instant;

@Service
public class FileService {

    @Value("${file.storage-dir}")
    private String storageDir;

    private final FileRepository repo;
    private final FileEventProducer producer;

    public FileService(FileRepository repo, FileEventProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    // ================= UPLOAD (ADMIN + TRAINER) =================
    public FileResource upload(MultipartFile file, String role) throws Exception {

        // normalize role
        role = role.toUpperCase();

        // role validation
        if (!role.equals("ADMIN") && !role.equals("TRAINER")) {
            throw new RuntimeException("Only ADMIN or TRAINER can upload files");
        }

        Path dir = Paths.get(storageDir);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        String storedName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = dir.resolve(storedName);

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        FileResource fr = new FileResource();
        fr.setOriginalName(file.getOriginalFilename());
        fr.setStoredName(storedName);
        fr.setContentType(file.getContentType());
        fr.setSize(file.getSize());
        fr.setUploadedByRole(role);
        fr.setUploadedAt(Instant.now());

        FileResource saved = repo.save(fr);

        // Kafka is OPTIONAL — should not break upload
        try {
            producer.sendFileUploadedEvent(saved.getId());
        } catch (Exception e) {
            System.err.println("Kafka skipped: " + e.getMessage());
        }

        return saved;
    }

    // ================= LIST =================
    @Cacheable(value = "files")
    public Page<FileResource> listFiles(Pageable pageable) {
        return repo.findAll(pageable);
    }

    // ================= DOWNLOAD =================
    public byte[] download(String storedName) throws Exception {
        Path path = Paths.get(storageDir).resolve(storedName);
        return Files.readAllBytes(path);
    }

    // ================= DELETE (ADMIN + TRAINER) =================
    @CacheEvict(value = "files", allEntries = true)
    public void delete(Long id, String role) throws Exception {

        role = role.toUpperCase();

        if (!role.equals("ADMIN") && !role.equals("TRAINER")) {
            throw new RuntimeException("Only ADMIN or TRAINER can delete files");
        }

        FileResource fr = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found: " + id));

        Path path = Paths.get(storageDir).resolve(fr.getStoredName());
        Files.deleteIfExists(path);

        repo.delete(fr);
    }
}
