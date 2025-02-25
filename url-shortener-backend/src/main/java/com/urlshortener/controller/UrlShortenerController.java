package com.urlshortener.controller;

import com.urlshortener.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public ResponseEntity<Map<String, String>> shortenUrl(@RequestParam String url, @RequestParam(defaultValue = "30") int expiryDays) {
        String shortCode = urlShortenerService.generateShortUrl(url, expiryDays);
        return ResponseEntity.ok(Map.of("shortUrl", "http://localhost:8080/api/" + shortCode));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> expandUrl(@PathVariable String shortCode) {
        return urlShortenerService.getOriginalUrl(shortCode)
                .map(url -> ResponseEntity.ok(Map.of("originalUrl", url)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
