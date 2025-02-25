package com.urlshortener.service;

import com.urlshortener.model.ShortUrl;
import com.urlshortener.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlCleanupService {

    private final ShortUrlRepository shortUrlRepository;
    private final StringRedisTemplate redisTemplate;
    private final S3BackupService s3BackupService;

    @Scheduled(cron = "0 0 * * * ?") //Runs every hour
    public void cleanupExpiredUrls() {
        List<ShortUrl> expiredShortCodes = shortUrlRepository.findExpiredShortCodes(LocalDateTime.now());

        if(!expiredShortCodes.isEmpty()) {
            expiredShortCodes.forEach(url -> {
                s3BackupService.backupExpiredUrls(url.getShortCode(), url.getOriginalUrl());

                redisTemplate.delete("shorturl:" + url.getShortCode());
        });

            shortUrlRepository.deleteAll(expiredShortCodes);

            System.out.println("Cleanup Completed: Removed " + expiredShortCodes.size() + " expired URLs.");
        }
    }
}
