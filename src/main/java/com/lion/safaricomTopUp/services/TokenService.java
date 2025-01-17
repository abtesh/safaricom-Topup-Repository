package com.lion.safaricomTopUp.services;

import com.lion.safaricomTopUp.dto.tokenDto.TokenRequest;
import com.lion.safaricomTopUp.dto.tokenDto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {


    @Value("${api.token.url}")
    private String tokenUrl;
    @Value("${api.token.ttl.minutes}")
    private int tokenTtl;
    private final WebClient.Builder webClientBuilder;
    private final StringRedisTemplate stringRedisTemplate;
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    private static final String TOKEN_KEY = "api_token";

    public String getToken(String correlationId) {
        try {
            String token = stringRedisTemplate.opsForValue().get(TOKEN_KEY);
            if (token != null) {
                return token;
            }

            // Call generate token API with correlation ID
            TokenResponse tokenResponse = generateToken(correlationId).block(); // Using block() to get the result
            if (tokenResponse != null && tokenResponse.getToken() != null) {
                token = tokenResponse.getToken();
                stringRedisTemplate.opsForValue().set(TOKEN_KEY, token, tokenTtl, TimeUnit.MINUTES);
                return token;
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching or storing token: {}", e.getMessage());
        }
        return null;
    }

    public Mono<TokenResponse> generateToken(String correlationId) {
        TokenRequest tokenRequest = TokenRequest.builder()
                .userName("PRETUPS") // Replace with actual username
                .password("MRyhsEPWzO8jDqaE0EAKnw==") // Replace with actual password
                .build();

        return webClientBuilder.build()
                .post()
                .uri(tokenUrl)
                .header("x-correlation-conversationid", "Lion-" + correlationId) // Dynamic correlation ID
                .bodyValue(tokenRequest)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnError(e -> log.error("Error fetching token: {}", e.getMessage()));
    }
}