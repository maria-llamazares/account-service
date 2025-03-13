package com.banking.account_service;

import com.banking.account_service.entities.Account;
import com.banking.account_service.repositories.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initialize(AccountRepository accountRepository) {
        return args -> {

            // Create accounts for the database (DB)
            Map<String, BigDecimal> balances = new HashMap<>();
            balances.put("USD", new BigDecimal("1000.00"));
            balances.put("EUR", new BigDecimal("500.00"));

            Account account = new Account();
            account.setIban("EE3822002210201578458065");
            account.setBalances(balances);

            accountRepository.save(account);

            balances = new HashMap<>();
            balances.put("GBP", new BigDecimal("100.00"));
            balances.put("IDR", new BigDecimal("10000000.00"));

            account = new Account();
            account.setIban("ES6112343456420456323532");
            account.setBalances(balances);

            accountRepository.save(account);


        };


    }
}

