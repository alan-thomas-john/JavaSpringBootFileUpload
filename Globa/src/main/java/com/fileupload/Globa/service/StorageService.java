package com.fileupload.Globa.service;

import com.fileupload.Globa.entity.Consumer;
//import com.fileupload.Globa.entity.ConsumerImg;
import com.fileupload.Globa.entity.ImageData;
import com.fileupload.Globa.entity.consumerImgTable;
import com.fileupload.Globa.repository.ConsumerImgRepository;
import com.fileupload.Globa.repository.ConsumerRepository;
import com.fileupload.Globa.repository.StorageRepository;
import com.fileupload.Globa.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ConsumerImgRepository consumerImgRepository;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/png", "image/jpeg", "image/jpg", "application/pdf"
    );
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Transactional
    public String uploadConsumerFiles(String consumerName, List<MultipartFile> files, List<String> imgFiles) throws IOException {
        if (files.size() != imgFiles.size()) {
            throw new IllegalArgumentException("Number of files and imgFile types must match");
        }

        Consumer consumer = Consumer.builder()
                .name(consumerName)
                .build();
        consumer = consumerRepository.save(consumer);

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            String imgFile = imgFiles.get(i);

            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty");
            }
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File size exceeds 5MB limit");
            }
            String contentType = file.getContentType();
            if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
                throw new IllegalArgumentException("Invalid file type. Only PNG, JPG, JPEG, and PDF are allowed.");
            }

            ImageData imageData = storageRepository.save(ImageData.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes()))
                    .build());

            consumerImgTable consumerImg = consumerImgTable.builder()
                    .consumer(consumer)
                    .imageData(imageData)
                    .imgFile(imgFile)
                    .build();
            consumerImgRepository.save(consumerImg);
        }

        return "Consumer " + consumer.getName() + " created with ID: " + consumer.getId() + " and files uploaded successfully";
    }

    @Transactional(readOnly = true)
    public byte[] downloadFile(Long id) {
        Optional<ImageData> dbFileData = storageRepository.findById(id);
        if (dbFileData.isPresent()) {
            return ImageUtils.decompressImage(dbFileData.get().getImageData());
        } else {
            throw new RuntimeException("File not found with id: " + id);
        }
    }

    @Transactional(readOnly = true)
    public String getFileContentType(Long id) {
        Optional<ImageData> dbFileData = storageRepository.findById(id);
        if (dbFileData.isPresent()) {
            return dbFileData.get().getType();
        } else {
            throw new RuntimeException("File not found with id: " + id);
        }
    }

    @Transactional(readOnly = true)
    public List<ConsumerImageResponse> getConsumerImages(Long consumerId) {
        Optional<Consumer> consumerOpt = consumerRepository.findById(consumerId);
        if (!consumerOpt.isPresent()) {
            throw new RuntimeException("Consumer not found with id: " + consumerId);
        }

        List<consumerImgTable> consumerImages = consumerImgRepository.findByConsumer(consumerOpt.get());
        return consumerImages.stream()
                .map(img -> new ConsumerImageResponse(
                       // img.getConsumer().getId(),
                        img.getConsumer().getName(),
                        img.getImageData().getId(),
                        img.getImageData().getName(),
                        img.getImageData().getType(),
                        img.getImgFile(),
                        ImageUtils.decompressImage(img.getImageData().getImageData())
                ))
                .collect(Collectors.toList());
    }
}

