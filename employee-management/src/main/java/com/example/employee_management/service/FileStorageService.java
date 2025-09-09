package com.example.employee_management.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) throws IOException {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation); // folder create if not exists
        logger.info("File storage location set to: {}", this.fileStorageLocation);
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        logger.info("Uploading file: {}", fileName);

        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        logger.info("File saved successfully: {}", targetLocation);
        return fileName;
    }

    public Path loadFile(String fileName) {
        logger.info("Loading file: {}", fileName);
        return this.fileStorageLocation.resolve(fileName).normalize();
    }
}
