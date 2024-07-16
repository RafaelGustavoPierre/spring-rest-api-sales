package com.rafael.sales.domain.service;

import java.util.UUID;

public interface StorageService {

    default String generateFileName(String fileName) {
        return UUID.randomUUID().toString() + "_" + fileName;
    }

}
