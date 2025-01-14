package com.lion.safaricomTopUp.dto.RechargeDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Payment {
    private String customerReference;
    private String customerReferenceType;
    private String customerName;
    private String date;
    private PaymentDetails paymentDetails;

}
