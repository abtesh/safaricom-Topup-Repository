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
public class TokenRequest {
    @JsonProperty("username")
    private String userName;
    private String password;
}
