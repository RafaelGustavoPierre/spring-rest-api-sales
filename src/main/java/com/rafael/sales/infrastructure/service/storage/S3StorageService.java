package com.rafael.sales.infrastructure.service.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.rafael.sales.core.storage.StorageProperties;
import com.rafael.sales.domain.exception.StorageException;
import com.rafael.sales.domain.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.ClassLoaderAwareGeneratorStrategy;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;

public class S3StorageService implements StorageService {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private StorageProperties storageProperties;

    @Override
    public MediaRecover recorver(String fileName) {
        String filePath = getPathFile(fileName);

        URL url = amazonS3.getUrl(storageProperties.getS3().getBucket(), filePath);

        return MediaRecover.builder().url(url.toString()).build();
    }

    @Override
    public void sendS3(File file) {
        try {
            String path = getPathFile(file.getFileName());
            var objectMetaData = new ObjectMetadata();
            objectMetaData.setContentType(file.getContentType());

            var putObjectRequest = new PutObjectRequest(
                    storageProperties.getS3().getBucket(),
                    path,
                    file.getInputStream(),
                    objectMetaData
            ).withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new StorageException("Não foi possivel subir arquivo para amazon S3", e);
        }
    }

    @Override
    public void removeS3(String fileName) {
        try {
            String fileDirectory = getPathFile(fileName);

            var deleteObjectRequest = new DeleteObjectRequest(storageProperties.getS3().getBucket(), fileDirectory);
            amazonS3.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            throw new StorageException("Não foi possivel remover o arquivo da S3", e);
        }
    }

    private String getPathFile(String fileName) {
        return String.format("%s/%s", storageProperties.getS3().getBucketPath(), fileName);
    }
}