package com.lion.safaricomTopUp.dto.tokenDto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TokenResponse {
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Desc")
    private String desc;
    @JsonProperty("Token")
    private String token;
    private String expires_on;
}
