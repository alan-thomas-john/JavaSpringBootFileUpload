package com.fileupload.Globa.controller;

import com.fileupload.Globa.service.ConsumerImageResponse;
import com.fileupload.Globa.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    StorageService services;

    @PostMapping("/upload-consumer")
    public ResponseEntity<?> uploadConsumerFiles(
            @RequestParam("consumerName") String consumerName,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("imgFiles") List<String> imgFiles) {
        try {
            String uploadResult = services.uploadConsumerFiles(consumerName, files, imgFiles);
            return ResponseEntity.status(HttpStatus.OK).body(uploadResult);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading files: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Long id) {
        try {
            byte[] fileData = services.downloadFile(id);
            String contentType = services.getFileContentType(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(fileData);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/consumer/{consumerId}")
    public ResponseEntity<?> getConsumerImages(@PathVariable Long consumerId) {
        try {
            List<ConsumerImageResponse> images = services.getConsumerImages(consumerId);
            return ResponseEntity.status(HttpStatus.OK).body(images);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }
}