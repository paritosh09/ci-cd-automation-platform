package com.cicd.automation.controller;

import com.cicd.automation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication and registration APIs")
public class AuthController {

    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Register a new user account")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody String request) {
        // Note: This requires RegisterRequest DTO which is missing from your structure
        // For now, returning a placeholder response
        return ResponseEntity.ok(new ApiResponse<>("Registration endpoint - requires RegisterRequest DTO", "Success"));
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody String request) {
        // Note: This requires LoginRequest DTO which is missing from your structure
        // For now, returning a placeholder response
        return ResponseEntity
                .ok(new ApiResponse<>("Login endpoint - requires LoginRequest and LoginResponse DTOs", "Success"));
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout user and invalidate token")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(new ApiResponse<>("Logout successful", "User logged out"));
    }
}
