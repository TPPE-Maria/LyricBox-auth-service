package com.microservices.authservice.dto;

public class JwtResponseDto {
    
    private String accessToken;
    private String tokenType = "Bearer";
    private Long id;
    private String role;
    
    // Constructors
    public JwtResponseDto() {}
    
    public JwtResponseDto(String accessToken, Long id, String role) {
        this.accessToken = accessToken;
        this.id = id;
        this.role = role;
    }
    
    // Getters and Setters
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}