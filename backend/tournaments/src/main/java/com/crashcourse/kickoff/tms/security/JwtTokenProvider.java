package com.crashcourse.kickoff.tms.security;

import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class JwtTokenProvider {

    public String getToken(String token) {
        return token.substring(7);
    }

}

