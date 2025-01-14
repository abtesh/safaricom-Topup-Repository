package com.lion.safaricomTopUp.dto.tokenDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TokenResponse {
    private String status;
    private String desc;
    private String token;
    private String expires_on;
}
