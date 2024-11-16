package com.gym_app.core.services;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import jakarta.xml.bind.DatatypeConverter;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtTokenProvider {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long expiration;
    private final ConcurrentHashMap<String, String> validTokens = new ConcurrentHashMap<>();
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    public String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        validTokens.put(username, token);
        return token;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    private Key getSignKey() {
        byte[] keyBytes = DatatypeConverter.parseBase64Binary(secretKey);
        return new SecretKeySpec(keyBytes, signatureAlgorithm.getJcaName());
    }

    @Synchronized
    public boolean isValid(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            return validTokens.containsKey(username) && validTokens.get(username).equals(token);
        } catch (ExpiredJwtException e) {
            System.out.println("Token has expired: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    public void invalidateToken(String token) {
        String userName = getUsernameFromToken(token);
        validTokens.remove(userName);
    }

    @Scheduled(fixedRate = 60000) // Runs every minute
    public void cleanupExpiredTokens() {
        validTokens.entrySet().removeIf(entry -> {
            String token = entry.getValue();
            try {
                Jwts.parserBuilder()
                        .setSigningKey(getSignKey())
                        .build()
                        .parseClaimsJws(token);
                return false; // Token is still valid
            } catch (ExpiredJwtException e) {
                return true; // Remove expired token
            }
        });
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}

