package com.lion.safaricomTopUp.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopUpService {
    private final TokenService tokenService;
    private final BalanceService balanceService;

//    public String topUp(String serviceNo, String amount) {
//        String token = tokenService.getToken();
//        String response = rechargeService.recharge(token, serviceNo, amount);
//        return response;
//    }
//
//    public String getBalance(String serviceNo) {
//        String token = tokenService.getToken();
//        String response = balanceService.getBalance(token, serviceNo);
//        return response;
//    }
}
