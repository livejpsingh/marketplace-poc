package com.jitendra.marketplace.auth.dto;

import java.util.List;



public class AuthResponse {
    private String accessToken;
    private String tokenType;
    private long expiresInSeconds;
    private String email;
    private String fullName;
    private List<String> roles;

    public AuthResponse() {
    }

    public AuthResponse(String accessToken, String tokenType, long expiresInSeconds, String email, String fullName, List<String> roles) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresInSeconds = expiresInSeconds;
        this.email = email;
        this.fullName = fullName;
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public List<String> getRoles() {
        return roles;
    }
}
