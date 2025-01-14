package com.lion.safaricomTopUp.controller;


import com.lion.safaricomTopUp.dto.MobileAppDto.MobileAppRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topUp")
public class TopUpController {

    private ResponseEntity<String> topUpRequest(@RequestBody MobileAppRequestDto mobileAppRequestDto) {

        return ResponseEntity.ok("Top up successful");
    }
}
