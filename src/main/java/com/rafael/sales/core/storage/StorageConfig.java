package com.rafael.sales.core.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.rafael.sales.domain.service.StorageService;
import com.rafael.sales.infrastructure.service.storage.S3StorageService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Autowired
    private StorageProperties storageProperties;

    @Bean
    public AmazonS3 amazonS3() {
        System.out.println(storageProperties.getS3().toString());
        var credentials = new BasicAWSCredentials(
                storageProperties.getS3().getIdAccessKey(),
                storageProperties.getS3().getSecretAccessKey());
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(storageProperties.getS3().getRegion())
                .build();
    }

    @Bean
    public StorageService storageService() {
        return new S3StorageService();
    }

}
