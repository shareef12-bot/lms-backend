package com.lms.file.repository;

import com.lms.file.model.FileResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileResource, Long> {
}
