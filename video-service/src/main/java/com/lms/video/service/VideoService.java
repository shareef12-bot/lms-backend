//package com.lms.video.service;
//
//import com.lms.video.kafka.VideoProducer;   // <-- ADD THIS
//import java.util.List;
//import com.lms.video.model.Video;
//import com.lms.video.repository.VideoRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.data.domain.Sort;
//
//
//import java.nio.file.*;
//
//@Service
//public class VideoService {
//
//    @Value("${video.upload-dir}")
//    private String uploadDir;
//
//    private final VideoRepository repo;
//    private final VideoProducer videoProducer;   // <-- ADD THIS
//
//    public VideoService(VideoRepository repo, VideoProducer videoProducer) {
//        this.repo = repo;
//        this.videoProducer = videoProducer;      // <-- ADD THIS
//    }
//
//    public Video uploadVideo(MultipartFile file) throws Exception {
//
//        Path directory = Paths.get(uploadDir);
//
//        if (!Files.exists(directory)) {
//            Files.createDirectories(directory);
//        }
//
//        String storedFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//        Path filePath = directory.resolve(storedFileName);
//
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//        String videoUrl = "http://localhost:9000/api/video/play/" + storedFileName;
//
//        Video video = new Video();
//        video.setOriginalFileName(file.getOriginalFilename());
//        video.setStoredFileName(storedFileName);
//        video.setVideoUrl(videoUrl);
//        video.setSize(file.getSize());
//
//        Video saved = repo.save(video);
//
//        // 🔥🔥 SEND KAFKA EVENT AFTER SUCCESSFUL UPLOAD
//        videoProducer.sendVideoUploadedEvent(storedFileName);
//
//        System.out.println("Kafka Event Sent for File: " + storedFileName);
//
//        return saved;
//    }
//
//    public byte[] getVideoFile(String fileName) throws Exception {
//        Path path = Paths.get(uploadDir).resolve(fileName);
//        return Files.readAllBytes(path);
//    }
//
//    public Video getVideoMeta(Long id) {
//        return repo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Video not found"));
//    }
//    public List<Video> getAllVideos() {
//        return repo.findAll(Sort.by(Sort.Direction.DESC, "uploadedAt"));
//    }
//}
//
//package com.lms.video.service;
//
//import com.lms.video.kafka.VideoProducer;
//import com.lms.video.model.Video;
//import com.lms.video.repository.VideoRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.data.domain.Sort;
//
//import java.io.IOException;
//import java.nio.file.*;
//import java.util.List;
//
//@Service
//public class VideoService {
//
//    @Value("${video.upload-dir}")
//    private String uploadDir;
//
//    private final VideoRepository repo;
//    private final VideoProducer videoProducer;
//
//    public VideoService(VideoRepository repo, VideoProducer videoProducer) {
//        this.repo = repo;
//        this.videoProducer = videoProducer;
//    }
//
//    public Video uploadVideo(MultipartFile file) throws Exception {
//
//        Path directory = Paths.get(uploadDir);
//
//        if (!Files.exists(directory)) {
//            Files.createDirectories(directory);
//        }
//
//        String storedFileName =
//                System.currentTimeMillis() + "_" + file.getOriginalFilename();
//
//        Path filePath = directory.resolve(storedFileName);
//
//        Files.copy(file.getInputStream(), filePath,
//                StandardCopyOption.REPLACE_EXISTING);
//
//        String videoUrl =
//                "http://localhost:9000/api/video/play/" + storedFileName;
//
//        Video video = new Video();
//        video.setOriginalFileName(file.getOriginalFilename());
//        video.setStoredFileName(storedFileName);
//        video.setVideoUrl(videoUrl);
//        video.setSize(file.getSize());
//
//        Video saved = repo.save(video);
//
//        // 🔥 Kafka upload event
//        videoProducer.sendVideoUploadedEvent(storedFileName);
//
//        return saved;
//    }
//
//    public byte[] getVideoFile(String fileName) throws Exception {
//        Path path = Paths.get(uploadDir).resolve(fileName);
//        return Files.readAllBytes(path);
//    }
//
//    public Video getVideoMeta(Long id) {
//        return repo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Video not found"));
//    }
//
//    public List<Video> getAllVideos() {
//        return repo.findAll(
//                Sort.by(Sort.Direction.DESC, "uploadedAt")
//        );
//    }
//
//    // ✅ FIXED DELETE LOGIC (ONLY THIS PART)
//    public void deleteVideo(Long id) {
//
//        Video video = repo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Video not found"));
//
//        // 🔥 Delete physical file using uploadDir
//        Path videoPath =
//                Paths.get(uploadDir).resolve(video.getStoredFileName());
//
//        try {
//            Files.deleteIfExists(videoPath);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to delete video file", e);
//        }
//
//        // 🔥 Delete DB record
//        repo.delete(video);
//
//        // 🔁 Kafka delete event (DO NOT FAIL REQUEST)
//        try {
//            videoProducer.sendVideoDeletedEvent(video.getStoredFileName());
//        } catch (Exception e) {
//            System.out.println(
//                "Kafka down. Video deleted without event: " + e.getMessage()
//            );
//        }
//    }
//}




package com.lms.video.service;

import com.lms.video.kafka.VideoProducer;
import com.lms.video.model.Video;
import com.lms.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class VideoService {

    @Value("${video.upload-dir}")
    private String uploadDir;

    private final VideoRepository repo;
    private final VideoProducer videoProducer;

    public VideoService(VideoRepository repo, VideoProducer videoProducer) {
        this.repo = repo;
        this.videoProducer = videoProducer;
    }

    public Video uploadVideo(MultipartFile file) throws Exception {

        Path directory = Paths.get(uploadDir);

        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        String storedFileName =
                System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path filePath = directory.resolve(storedFileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        Video video = new Video();
        video.setOriginalFileName(file.getOriginalFilename());
        video.setStoredFileName(storedFileName);
        video.setSize(file.getSize());

        Video saved = repo.save(video);

        // 🔥 Kafka upload event
        videoProducer.sendVideoUploadedEvent(storedFileName);

        return saved;
    }

    public byte[] getVideoFile(String fileName) throws Exception {
        Path path = Paths.get(uploadDir).resolve(fileName);
        return Files.readAllBytes(path);
    }

    public Video getVideoMeta(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));
    }

    public List<Video> getAllVideos() {
        return repo.findAll(
                Sort.by(Sort.Direction.DESC, "uploadedAt")
        );
    }

    public void deleteVideo(Long id) {

        Video video = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        Path videoPath =
                Paths.get(uploadDir).resolve(video.getStoredFileName());

        try {
            Files.deleteIfExists(videoPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete video file", e);
        }

        repo.delete(video);

        try {
            videoProducer.sendVideoDeletedEvent(video.getStoredFileName());
        } catch (Exception e) {
            System.out.println(
                "Kafka down. Video deleted without event: " + e.getMessage()
            );
        }
    }
}
