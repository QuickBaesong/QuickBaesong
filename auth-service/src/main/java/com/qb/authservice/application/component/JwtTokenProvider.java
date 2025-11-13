package com.qb.authservice.application.component;

import com.qb.authservice.domain.entity.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60; // 1시간
    private final SecretKey key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /** Access Token 생성 */
    public String createAccessToken(UUID userId, String username, UserRole role) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiryDate = Date.from(now.plusMillis(ACCESS_TOKEN_EXPIRATION_TIME));

        return Jwts.builder()
                .subject(userId.toString())
                .issuer("auth-service")
                .claim("username", username)
                .claim("role", role.name())
                .issuedAt(issuedAt)
                .expiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}

