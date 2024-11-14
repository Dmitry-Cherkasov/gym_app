package com.gym_app.core.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
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
    private final ConcurrentHashMap<String, String> validTokens  = new ConcurrentHashMap<>();
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
        Claims claims =  Jwts.parserBuilder()
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

    public boolean isValid(String token) {
        if(!validTokens.contains(token)) return false;
        try {
            Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void invalidateToken(String token){
        String userName = getUsernameFromToken(token);
        validTokens.remove(token);
    }

}

