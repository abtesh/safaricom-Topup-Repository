package com.lion.safaricomTopUp.services;

import com.lion.safaricomTopUp.dto.tokenDto.TokenRequest;
import com.lion.safaricomTopUp.dto.tokenDto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${api.token.url}")
    private String tokenUrl;
    @Value("${api.username}")
    private String username;
    @Value("${api.password}")
    private String password;
    @Value("${api.token.ttl.minutes}")
    private int tokenTtl;
    private final RestTemplate restTemplate;
    private final StringRedisTemplate stringRedisTemplate;
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    private static final String TOKEN_KEY = "api_token";

    public String getToken() {
        try {
            String token = stringRedisTemplate.opsForValue().get(TOKEN_KEY);
            if (token != null) {
                return token;
            }
            TokenResponse tokenResponse = generateToken(username, password);
            if (tokenResponse != null && tokenResponse.getToken() != null) {
                token = tokenResponse.getToken();
                stringRedisTemplate.opsForValue().set(TOKEN_KEY, token, 10, TimeUnit.MINUTES);
                return token;
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching or storing token: {}", e.getMessage());
        }
        return null;
    }

    public TokenResponse generateToken(String username, String password) {
        TokenRequest tokenRequest = TokenRequest.builder()
                .userName(username)
                .password(password)
                .build();
        return restTemplate.postForObject(tokenUrl, tokenRequest, TokenResponse.class);
    }
}
