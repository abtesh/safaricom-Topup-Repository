package com.lion.safaricomTopUp.controller;

import com.lion.safaricomTopUp.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final TokenService tokenService;

    private String correlationId;

    @GetMapping("/token")
    public Mono<ResponseEntity<String>> testToken() {
//        String token = tokenService.getToken("1234");
//        if (token != null) {
//            return ResponseEntity.ok("Token retrieved successfully: " + token);
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve token.");
//        }

        return tokenService.getToken(correlationId)
                .map(token -> ResponseEntity.ok("Token retrieved successfully: " + token))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve token."));
    }
}