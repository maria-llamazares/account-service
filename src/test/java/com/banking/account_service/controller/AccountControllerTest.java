package com.banking.account_service.controller;

import com.banking.account_service.dto.TransactionRequest;
import com.banking.account_service.entities.Account;
import com.banking.account_service.services.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    void deposit_WhenValidRequest_ShouldReturnUpdatedAccount() throws Exception {
        String iban = "TESTIBAN123456";
        TransactionRequest request = new TransactionRequest();
        request.setCurrency("USD");
        request.setAmount(BigDecimal.valueOf(100));

        Account updatedAccount = new Account();
        updatedAccount.setId(1L);
        Map<String, BigDecimal> balances = new HashMap<>();
        balances.put("USD", BigDecimal.valueOf(1000));
        updatedAccount.setBalances(balances);

        Mockito.when(accountService.deposit(eq(iban), eq("USD"), eq(BigDecimal.valueOf(100)))).thenReturn(updatedAccount);

        mockMvc.perform(post("/accounts/{iban}/deposit", iban)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "currency": "USD",
                                        "amount": 100
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.balances.USD").value(1000));
    }

    @Test
    void deposit_WhenInvalidCurrency_ShouldReturnBadRequest() throws Exception {
        String iban = "TESTIBAN123456";

        mockMvc.perform(post("/accounts/{iban}/deposit", iban)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "currency": "",
                                        "amount": 100
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("Currency is required."));
    }

    @Test
    void deposit_WhenAmountBelowMinimum_ShouldReturnBadRequest() throws Exception {
        String iban = "TESTIBAN123456";

        mockMvc.perform(post("/accounts/{iban}/deposit", iban)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "currency": "USD",
                                        "amount": 5
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("Amount must be at least 10."));
    }
}