package com.urlshortener.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class S3BackupService {

    private static final Logger logger = LoggerFactory.getLogger(S3BackupService.class);

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key}")
    private String accessKey;

    @Value("${aws.s3.secret-key}")
    private String secretKey;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
                .build();
    }

    public void backupExpiredUrls(String shortCode, String originalUrl) {
        try {
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
            String objectKey = "expired-urls/expired-" + timeStamp + ".json";

            String jsonContent = String.format("""
                    {
                        "shortCode": "%s",
                        "originalUrl": "%s",
                        "deletedAt": "%s"
                    }
                    """, shortCode, originalUrl, timeStamp);

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .contentType("application/json")
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(new ByteArrayInputStream(jsonContent.getBytes(StandardCharsets.UTF_8)), jsonContent.length())
            );

            logger.info("Expired URL backed up successfully: {}", objectKey);
        } catch (Exception e) {
            logger.error("Error occurred while backing up expired URL: {}", e.getMessage(), e);
        }
    }
}