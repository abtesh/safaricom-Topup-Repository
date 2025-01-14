package com.lion.safaricomTopUp.dto.MobileAppDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MobileAppRequestDto {
    private String serviceNo;
    private String amount;
}
