package com.lion.safaricomTopUp.dto.BalanceDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class products {
    private String code;
    private String shortname;
    private String balance;
}
