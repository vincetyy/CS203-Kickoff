package com.crashcourse.kickoff.tms.user.dto;

public class LoginResponseDTO {
    private Long userId;
    private String jwtToken;

    public LoginResponseDTO(Long userId, String jwtToken) {
        this.userId = userId;
        this.jwtToken = jwtToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}