package com.apapedia.user.security;

import java.security.Key;
import java.util.Date;

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
    public String generateToken(String username, String role) {
        Date currentDate = new Date();
        // Expires in 5 hours
        Date expiredDate = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000);
        // Generate token
        String token = Jwts.builder().setSubject(username).claim("role", role).setIssuedAt(currentDate)
                .setExpiration(expiredDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        return token;
    }

    // Get username from token
    public String getUsernameFromJWT(String token) {
        Claims claim = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        return claim.getSubject();
    }

    public String getIdFromJWT(String token) {
        Claims claim = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        return claim.get("id", String.class);
    }

    public String getRoleFromJWT(String token) {
        Claims claim = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        return claim.get("role", String.class);
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
