package com.lion.safaricomTopUp.dto.RechargeDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Id {
    private String value;
    private String schemeName;

}
