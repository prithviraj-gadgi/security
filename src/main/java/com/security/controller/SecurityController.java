package com.security.controller;

import com.security.dto.LoginRequest;
import com.security.dto.LoginResponse;
import com.security.service.JWTService;
import com.security.utility.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/jwt")
@Tag(name = "Security Controller", description = "Controller for security operations")
public class SecurityController {

    private final JWTService jwtService;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate;

    @Autowired
    public SecurityController(JWTService jwtService, JWTUtil jwtUtil, AuthenticationManager authenticationManager, RestTemplate restTemplate) {
        this.jwtService = jwtService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.restTemplate = restTemplate;
    }

    @Operation(summary = "Generate JWT", description = "Endpoint to generate JWT token")
    @PostMapping("/generate")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        LoginResponse loginResponse = new LoginResponse();
        if (authentication.isAuthenticated()) {
            loginResponse.setToken(jwtUtil.generateToken(loginRequest.getUsername()));
            loginResponse.setMessage("Welcome, login successful!");
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            loginResponse.setMessage("Invalid username/password!");
            return new ResponseEntity<>(loginResponse, HttpStatus.UNAUTHORIZED);
        }

    }

}
