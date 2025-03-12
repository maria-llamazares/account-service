package com.banking.account_service.controller;

import com.banking.account_service.dto.AccountBalanceResponse;
import com.banking.account_service.dto.TransactionRequest;
import com.banking.account_service.entities.Account;
import com.banking.account_service.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts/")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("{iban}/balances")
    public AccountBalanceResponse getBalances(@PathVariable String iban) {
        return accountService.getBalance(iban);
    }

    @PostMapping("{iban}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable String iban, @RequestBody @Valid TransactionRequest request) {
        Account updatedAccount = accountService.deposit(iban, request.getCurrency(), request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }

    @PostMapping("{iban}/debit")
    public ResponseEntity<Account> debit(@PathVariable String iban, @RequestBody @Valid TransactionRequest request) {
        Account updatedAccount = accountService.debit(iban, request.getCurrency(), request.getAmount());
        return ResponseEntity.ok(updatedAccount);
    }
}


