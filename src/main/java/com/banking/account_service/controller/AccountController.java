package com.banking.account_service.controller;

import com.banking.account_service.dto.AccountBalanceResponse;
import com.banking.account_service.dto.TransactionRequest;
import com.banking.account_service.entities.Account;
import com.banking.account_service.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Bank Account Management API",
                version = "1.0",
                description = "The Bank Account Balance Management API allows users to check account balances, perform deposits, and execute debits. With this API, users can manage their account balances through simple operations such as adding funds (deposits) or subtracting balance (debits), making it a key tool for digital financial management. ",
                contact = @Contact(name = "Maria Llamazares", email = "mariallamazareslopez@gmail.com")
        )
)

@RestController
@RequestMapping("/accounts")

public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Check account balance.", description = "Retrieves the current balance of the account using its IBAN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account balance retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountBalanceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class,
                                    example = """
                                            {
                                              "dateTimeStamp": "2025-03-13 09:39:50",
                                              "status": 404,
                                              "errorCode": "IBAN_NOT_FOUND",
                                              "message": "Account with IBAN 'GB3822002219020157845806' not found.",
                                              "path": "/accounts/GB3822002210201578459806/balances"
                                            }""")))
    })
    @GetMapping("/{iban}/balances")
    public AccountBalanceResponse getBalances(@Parameter(description = "The IBAN of the bank account.", example = "EE3822002210201578458065") @PathVariable String iban) {
        return accountService.getBalance(iban);
    }

    @Operation(summary = "Deposit funds into a bank account.", description = "Deposits the specified amount of money into the bank account associated with the given IBAN. The amount should be provided in the request body along with the currency.")
    @PostMapping("/{iban}/deposit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account balance retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountBalanceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parameter format invalid",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "Parameter format invalid", value = """
                                {
                                  "dateTimeStamp": "2025-03-13 10:17:57",
                                  "status": 400,
                                  "errorCode": "ERROR_PARAMETER_FORMAT_INVALID",
                                  "message": "Parameter: 'amount'. Cannot deserialize value of type `java.math.BigDecimal` from String 'X': not a valid representation",
                                  "path": "uri=/accounts/EE3822002210201578458065/deposit"
                                }""")
                            })),
            @ApiResponse(responseCode = "404", description = "Account or Currency not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "IBAN Not Found", value = """
                                            {
                                              "dateTimeStamp": "2025-03-13 09:39:50",
                                              "status": 404,
                                              "errorCode": "IBAN_NOT_FOUND",
                                              "message": "Account with IBAN 'GB3822002219020157845806' not found.",
                                              "path": "/accounts/GB3822002210201578459806/balances"
                                            }"""),
                                    @ExampleObject(name = "Currency Not Found", value = """
                                            {
                                              "dateTimeStamp": "2025-03-13 09:58:23",
                                              "status": 404,
                                              "errorCode": "CURRENCY_NOT_FOUND",
                                              "message": "Currency 'X' not found.",
                                              "path": "/accounts/EE3822002210201578458065/deposit"
                                            }""")
                            }))
    })
    public ResponseEntity<Account> deposit(@Parameter(description = "The IBAN of the bank account.", example = "EE3822002210201578458065") @PathVariable String iban, @RequestBody @Valid TransactionRequest request) {
        Account updatedAccount = accountService.deposit(iban, request.getCurrency(), request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }

    @Operation(summary = "Debit funds from a bank account.", description = "Debits the specified amount of money from the bank account associated with the given IBAN. The amount to be debited and the currency should be provided in the request body.")
    @PostMapping("/{iban}/debit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account balance retrieved successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountBalanceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid currency provided for the transaction. / Parameter format invalid.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "Currency not available for IBAN", value = """
                                    {
                                      "dateTimeStamp": "2025-03-13 10:10:33",
                                      "status": 400,
                                      "errorCode": "ERROR_INVALID_CURRENCY",
                                      "message": "The currency 'IDR' is not available for IBAN 'EE3822002210201578458065'.",
                                      "path": "/accounts/EE3822002210201578458065/debit"
                                    }"""),
                                    @ExampleObject(name = "Parameter format invalid", value = """
                                    {
                                      "dateTimeStamp": "2025-03-13 10:17:57",
                                      "status": 400,
                                      "errorCode": "ERROR_PARAMETER_FORMAT_INVALID",
                                      "message": "Parameter: 'amount'. Cannot deserialize value of type `java.math.BigDecimal` from String 'X': not a valid representation",
                                      "path": "uri=/accounts/EE3822002210201578458065/deposit"
                                    }""")
                            })),
            @ApiResponse(responseCode = "404", description = "Account or Currency not found.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "IBAN Not Found", value = """
                                    {
                                      "dateTimeStamp": "2025-03-13 09:39:50",
                                      "status": 404,
                                      "errorCode": "IBAN_NOT_FOUND",
                                      "message": "Account with IBAN 'GB3822002219020157845806' not found.",
                                      "path": "/accounts/GB3822002210201578459806/balances"
                                    }"""),
                                    @ExampleObject(name = "Currency Not Found", value = """
                                    {
                                      "dateTimeStamp": "2025-03-13 09:58:23",
                                      "status": 404,
                                      "errorCode": "CURRENCY_NOT_FOUND",
                                      "message": "Currency 'X' not found.",
                                      "path": "/accounts/EE3822002210201578458065/deposit"
                                    }""")
                            })),
            @ApiResponse(responseCode = "422", description = "Insufficient funds for the transaction.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "Insufficient Funds", value = """
                                    {
                                      "dateTimeStamp": "2025-03-13 10:05:40",
                                      "status": 422,
                                      "errorCode": "INSUFFICIENT_FUNDS",
                                      "message": "Insufficient funds for IBAN 'EE3822002210201578458065'. The withdrawal amount of 100000 exceeds the available balance of 1000.00.",
                                      "path": "/accounts/EE3822002210201578458065/debit"
                                    }""")
                            }))
    })
    public ResponseEntity<Account> debit(@Parameter(description = "The IBAN of the bank account.", example = "EE3822002210201578458065") @PathVariable String iban, @RequestBody @Valid TransactionRequest request) {
        Account updatedAccount = accountService.debit(iban, request.getCurrency(), request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }
}


