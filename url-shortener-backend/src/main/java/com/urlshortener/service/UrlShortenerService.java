package com.urlshortener.service;

import com.urlshortener.model.ShortUrl;
import com.urlshortener.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final ShortUrlRepository shortUrlRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_CODE_LENGTH = 6;
    private static final String REDIS_PREFIX = "shorturl:";

    public String generateShortUrl(String originalUrl, int expiryDays) {
        System.out.println("Generating short URL for: " + originalUrl);
        String shortCode = generateRandomShortCode();
        LocalDateTime now = LocalDateTime.now();

        ShortUrl shortUrl = ShortUrl.builder()
                .shortCode(shortCode)
                .originalUrl(originalUrl)
                .createdAt(now)
                .expiresAt(expiryDays > 0 ? now.plusDays(expiryDays) : now.plusMinutes(1))
                .build();

        shortUrlRepository.save(shortUrl);
        System.out.println("Post DB Update");
        long expirySeconds = expiryDays > 0 ? Duration.ofDays(expiryDays).getSeconds() : 60;
        redisTemplate.opsForValue().set(REDIS_PREFIX + shortCode, originalUrl, Duration.ofSeconds(expirySeconds));
        System.out.println("Post redis Update");

        return shortCode;
    }

    public Optional<String> getOriginalUrl(String shortCode) {
        String cachedUrl = redisTemplate.opsForValue().get(REDIS_PREFIX + shortCode);
        if (cachedUrl != null) {
            return Optional.of(cachedUrl);
        }

        return shortUrlRepository.findByShortCode(shortCode)
                .filter(shortUrl -> shortUrl.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(shortUrl -> {
                    redisTemplate.opsForValue().set(REDIS_PREFIX + shortUrl.getShortCode(), shortUrl.getOriginalUrl(),
                            Duration.between(LocalDateTime.now(), shortUrl.getExpiresAt()));
                    return shortUrl.getOriginalUrl();
                });
    }

    private String generateRandomShortCode() {
        Random random = new Random();
        StringBuilder shortCode = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            shortCode.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return shortCode.toString();
    }
}