package com.lion.safaricomTopUp.dto.BalanceDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BalanceRequestDto {
    private String type;
    private List<Id> id;
    private String password;
}
