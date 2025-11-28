package com.microservices.authservice.client;

import com.microservices.authservice.dto.RegisterRequestDto;
import com.microservices.authservice.dto.UserResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserServiceClient {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);
    
    @Autowired
    private WebClient webClient;
    
    public UserServiceClient() {
    }
    
    public UserResponseDto createUser(RegisterRequestDto registerRequest) {
        logger.info("Calling user service to create user: {}", registerRequest.getUsername());
        
        // Converter RegisterRequestDto para o formato esperado pelo user-service
        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("username", registerRequest.getUsername());
        createRequest.put("email", registerRequest.getEmail());
        createRequest.put("password", registerRequest.getPassword());
        createRequest.put("name", registerRequest.getName());
        // O user-service espera role como String que será convertido para enum automaticamente
        // Usar role do request se fornecido, caso contrário usar USER como padrão
        String role = registerRequest.getRole() != null ? registerRequest.getRole().toUpperCase() : "USER";
        createRequest.put("role", role);
        
        logger.debug("Sending request to user-service with payload: {}", createRequest);
        
        try {
            
            UserResponseDto response = webClient.post()
                    .uri("/users/register")
                    .bodyValue(createRequest)
                    .retrieve()
                    .bodyToMono(UserResponseDto.class)
                    .block();
            
            logger.info("User created successfully: {}", registerRequest.getUsername());
            return response;
            
        } catch (WebClientResponseException e) {
            logger.error("HTTP error creating user in user-service: {} - {}", 
                        e.getStatusCode(), e.getResponseBodyAsString());
            logger.error("Request payload was: {}", createRequest);
            throw new RuntimeException("Failed to create user in user-service: HTTP " + 
                                     e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("Error communicating with user-service: {}", e.getMessage(), e);
            logger.error("Request payload was: {}", createRequest);
            throw new RuntimeException("Failed to create user in user-service: " + e.getMessage(), e);
        }
    }
    
    public UserLoginData getUserForLogin(String identifier) {
        try {
            logger.info("Calling user service to get login data for: {}", identifier);
            
            UserLoginData response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/users/auth/login-data")
                            .queryParam("identifier", identifier)
                            .build())
                    .retrieve()
                    .bodyToMono(UserLoginData.class)
                    .block();
            
            logger.info("Successfully retrieved login data for: {}", identifier);
            return response;
            
        } catch (WebClientResponseException e) {
            logger.error("HTTP error getting user from user-service: {} - {}", 
                        e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to get user from user-service: HTTP " + 
                                     e.getStatusCode() + " - " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error communicating with user-service: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get user from user-service: " + e.getMessage(), e);
        }
    }
    
    // DTO interno para receber dados de login do user-service
    public static class UserLoginData {
        private Long id;
        private String username;
        private String email;
        private String password;
        private String role;
        
        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}