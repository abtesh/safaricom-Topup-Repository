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

    public Mono<String> getToken(String correlationId) {
            String token = stringRedisTemplate.opsForValue().get(TOKEN_KEY);
            if (token != null) {
                return Mono.just(token);
            }

            // Call generate token API with correlation ID
           return this.generateToken(correlationId).flatMap(response -> {
               if(response!=null && response.getToken()!=null){
                   stringRedisTemplate.opsForValue().set(TOKEN_KEY, response.getToken(), tokenTtl, TimeUnit.MINUTES);
                   return Mono.just(response.getToken());
               }
               else{
                   return Mono.empty();
               }
           });

    }

    public Mono<TokenResponse> generateToken(String correlationId) {
        TokenRequest tokenRequest = TokenRequest.builder()
                .userName("PRETUPS") // Replace with actual username
                .password("MRyhsEPWzO8jDqaE0EAKnw==") // Replace with actual password
                .build();

        return webClientBuilder.build()
                .post()
                .uri(tokenUrl)
                .header("x-correlation-conversationid", "Lion-" + correlationId)// Dynamic correlation ID
                .header("x-source-system", "STEP")
                .header("x-source-identity-token", "U2FmYXJpY29tOmUyZTplc2JldDpBdXRvbWF0aW9u")
                .bodyValue(tokenRequest)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnError(e -> log.error("Error fetching token: {}", e.getMessage()));
    }
}