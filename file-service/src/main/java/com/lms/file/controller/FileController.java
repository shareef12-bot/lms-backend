package com.lms.file.controller;

import com.lms.file.model.FileResource;
import com.lms.file.service.FileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    // ================= UPLOAD =================
    @PostMapping("/upload")
    public FileResource upload(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-ROLE") String role
    ) throws Exception {
        return service.upload(file, role);
    }

    // ================= LIST =================
    @GetMapping
    public Page<FileResource> list(Pageable pageable) {
        return service.listFiles(pageable);
    }

    // ================= DOWNLOAD =================
    @GetMapping("/download/{name}")
    public ResponseEntity<byte[]> download(@PathVariable String name) throws Exception {

        byte[] data = service.download(name);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + name + "\"")
                .body(data);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("X-ROLE") String role
    ) throws Exception {

        service.delete(id, role);
        return ResponseEntity.noContent().build();
    }
}
