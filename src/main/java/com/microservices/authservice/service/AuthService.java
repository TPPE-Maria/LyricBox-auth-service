package com.microservices.authservice.service;

import com.microservices.authservice.client.UserServiceClient;
import com.microservices.authservice.dto.JwtResponseDto;
import com.microservices.authservice.dto.LoginRequestDto;
import com.microservices.authservice.dto.RegisterRequestDto;
import com.microservices.authservice.dto.UserResponseDto;
import com.microservices.authservice.exception.ResourceNotFoundException;
import com.microservices.authservice.exception.UserAlreadyExistsException;
import com.microservices.authservice.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private UserServiceClient userServiceClient;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public UserResponseDto register(RegisterRequestDto registerRequest) {
        try {
            logger.info("Starting user registration for username: {}", registerRequest.getUsername());
            
            // Call user service to create user (password will be hashed there)
            UserResponseDto userResponse = userServiceClient.createUser(registerRequest);
            
            logger.info("User registration successful for username: {}", registerRequest.getUsername());
            return userResponse;
            
        } catch (Exception e) {
            logger.error("Error during user registration for username: {}", registerRequest.getUsername(), e);
            throw new UserAlreadyExistsException("Error creating user: " + e.getMessage());
        }
    }
    
    public JwtResponseDto login(LoginRequestDto loginRequest) {
        try {
            logger.info("Starting login attempt for identifier: {}", loginRequest.getUsernameOrEmail());
            
            // Get user login data from user service
            UserServiceClient.UserLoginData userData = userServiceClient.getUserForLogin(loginRequest.getUsernameOrEmail());
            
            logger.debug("Retrieved user data for login: {}", userData.getUsername());
            
            // Verify password (user service returns hashed password)
            if (!passwordEncoder.matches(loginRequest.getPassword(), userData.getPassword())) {
                logger.warn("Invalid password attempt for user: {}", userData.getUsername());
                throw new ResourceNotFoundException("Invalid credentials");
            }

            // Generate JWT token
            String jwt = jwtUtil.generateToken(userData.getUsername(), userData.getId(), userData.getRole());

            logger.info("Login successful for user: {}", userData.getUsername());
            
            return new JwtResponseDto(jwt, userData.getId(), userData.getRole());
            
        } catch (Exception e) {
            logger.error("Error during login for identifier: {}", loginRequest.getUsernameOrEmail(), e);
            throw new ResourceNotFoundException("User not found or invalid credentials");
        }
    }
}