package com.rafael.sales.domain.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;
import java.util.UUID;

public interface StorageService {

    default String generateFileName(String fileName) {
        return UUID.randomUUID().toString() + "_" + fileName;
    }

    void sendS3(File file);

    default void save(File file) {
        this.sendS3(file);
    }

    @Builder
    @Getter
    class File {
        private String fileName;
        private String contentType;
        private Long size;
        private InputStream inputStream;
    }

}
