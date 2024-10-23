package com.crashcourse.kickoff.tms.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class JwtAuthService {

    private final JwtUtil jwtUtil;

    public JwtAuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<String> validateToken(String token, Long idToDelete) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Authorization token is missing or invalid");
        }

        token = token.substring(7); // Remove "Bearer " prefix
        Long userIdFromToken = jwtUtil.extractUserId(token);

        if (!idToDelete.equals(userIdFromToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to perform this action");
        }
        return null; // Indicating token validation is successful
    }
}
