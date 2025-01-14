package com.lion.safaricomTopUp.controller;

import com.lion.safaricomTopUp.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TokenService tokenService;

    @GetMapping("/token")
    public ResponseEntity<String> testToken() {
        String token = tokenService.getToken();
        if (token != null) {
            return ResponseEntity.ok("Token retrieved successfully: " + token);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve token.");
        }
    }
}