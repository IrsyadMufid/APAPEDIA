package com.apapedia.user.security;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGenerator {

    public static final String JWT_SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    // Generate token
    public String generateToken(String username, UUID id, String role) {
        var currentDate = new Date();
        // Expires in 5 hours
        var expiredDate = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000);
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
