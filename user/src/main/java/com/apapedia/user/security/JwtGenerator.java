package com.apapedia.user.security;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGenerator {

    @Value("${JWT_SECRET}")
    public String JWT_SECRET;

    @Value("${JWT_EXPIRES_IN}")
    public long JWT_EXPIRES_IN;

    // Generate token
    public String generateToken(String username, UUID id, String role) {
        var currentDate = new Date();
        // Expires in 5 hours
        var expiredDate = new Date(System.currentTimeMillis() + JWT_EXPIRES_IN * 1000);
        // Generate token
        return Jwts.builder()
                .setSubject(username)
                .claim("id", id.toString())
                .claim("role", role)
                .setIssuedAt(currentDate)
                .setExpiration(expiredDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Get username from token
    public String getUsernameFromJWT(String token) {
        Claims claim = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        return claim.getSubject();
    }

    public String getIdFromJWT(String token) {
        Claims claim = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        return (String) claim.get("id");
    }

    public String getRoleFromJWT(String token) {
        Claims claim = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        return (String) claim.get("role");
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT expired or incorrect");
        }
    }

    // Get sign key
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
