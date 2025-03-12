package com.banking.account_service.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceResponse {

    private String iban;
    private Map<String, BigDecimal> balances = new HashMap<>();

}