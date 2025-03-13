package com.banking.account_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "The IBAN of the bank account.",
            example = "EE3822002210201578458065")
    private String iban;

    @Schema(description = "Map of balances, where each key is the currency and the value is the balance amount.",
            example = "{\"USD\": 1000.0, \"EUR\": 500.0}")
    private Map<String, BigDecimal> balances = new HashMap<>();

}