package com.urlshortener.repository;

import com.urlshortener.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByShortCode(String shortCode);

    @Query("SELECT s FROM ShortUrl s WHERE s.expiresAt < :now")
    List<ShortUrl> findExpiredShortCodes(LocalDateTime now);

    @Transactional
    void deleteByShortCodeIn(List<String> shortCodes);
}
