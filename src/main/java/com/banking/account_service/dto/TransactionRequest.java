package com.banking.account_service.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "The currency of the amount to be debited, e.g., USD, EUR.",
            example = "USD")
    private String currency;

    @NotNull(message = Constants.MESSAGE_AMOUNT_REQUIRED)
    @DecimalMin(value = "10", message=Constants.MESSAGE_AMOUNT_MINIMUM)
    @Schema(description = "The amount to be debited from the account. The minimum amount is 10.",
            example = "100.00")
    private BigDecimal amount;

}
