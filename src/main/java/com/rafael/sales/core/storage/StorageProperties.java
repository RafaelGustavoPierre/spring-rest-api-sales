package com.rafael.sales.core.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("sales.storage")
public class StorageProperties {

    private S3 s3 = new S3();

    @Getter
    @Setter
    public class S3 {

        @Value("${sales.storage.s3.id-access-key}")
        private String idAccessKey;

        @Value("${sales.storage.s3.secret-access-key}")
        private String secretAccessKey;

        @Value("${sales.storage.s3.bucket}")
        private String bucket;

        @Value("${sales.storage.s3.region}")
        private String region;

        @Value("${sales.storage.s3.bucket-path}")
        private String bucketPath;

    }

}
