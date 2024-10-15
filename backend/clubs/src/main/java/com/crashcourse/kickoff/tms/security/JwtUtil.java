package com.crashcourse.kickoff.tms.security;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    // Lazily load the dotenv file only if needed
    private Dotenv dotenv;

    // Try to load JWT_SECRET_KEY from the system environment, fallback to dotenv if system env is null
    private String JWT_SECRET_KEY;

    public JwtUtil() {
        JWT_SECRET_KEY = System.getenv("JWT_SECRET_KEY");

        // If JWT_SECRET_KEY is null, load from dotenv
        if (JWT_SECRET_KEY == null) {
            dotenv = Dotenv.load();
            JWT_SECRET_KEY = dotenv.get("JWT_SECRET_KEY");  // Load from dotenv if system env is null
        }
    }

    private final long jwtExpirationInMillis = 3600000; // 1 hour in milliseconds

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY); 
        return Keys.hmacShaKeyFor(keyBytes); 
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        SecretKey key = getSigningKey();

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "Token has expired");
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}