package com.banking.account_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.banking.account_service.utils.Constants;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {

    @NotBlank(message = Constants.MESSAGE_CURRENCY_REQUIRED)
    private String currency;

    @NotNull(message = Constants.MESSAGE_AMOUNT_REQUIRED)
    @DecimalMin(value = "10", message=Constants.MESSAGE_AMOUNT_MINIMUM)
    private BigDecimal amount;

}
