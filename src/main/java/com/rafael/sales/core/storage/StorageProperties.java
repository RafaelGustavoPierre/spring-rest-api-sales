package com.rafael.sales.core.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sales.storage.s3")
public class StorageProperties {

    private S3 s3 = new S3();

    @Getter
    @Setter
    public class S3 {

        private String idAccessKey;
        private String secretAccessKey;
        private String bucket;
        private String region;
        private String bucketPath;

    }

}
