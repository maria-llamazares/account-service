package com.banking.account_service.error.utils;

import com.banking.account_service.entities.Account;
import com.banking.account_service.error.CustomException;
import com.banking.account_service.repositories.AccountRepository;
import com.banking.account_service.utils.Constants;
import com.banking.account_service.utils.AccountUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
class AccountUtilsTest {

    @InjectMocks
    private AccountUtils accountUtils;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void testValidateCurrency_validCurrency() {

        String currency = "EUR";
        assertDoesNotThrow(() -> accountUtils.validateCurrency(currency));

    }

    @Test
    void testValidateCurrency_invalidCurrency() {

        String currency = "X";

        CustomException exception = assertThrows(CustomException.class,
                () -> accountUtils.validateCurrency(currency)
        );

        assertEquals("Currency '" + currency + "' not found.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatus());
        assertEquals(Constants.ERROR_CURRENCY_NOT_FOUND, exception.getErrorCode());

    }

    @Test
    void testFindAccountByIbanOrThrow_validAccount() {

        String iban = "ES6112343456420456323532";
        Account account = new Account();
        account.setIban(iban);

        when(accountRepository.findByIban(iban)).thenReturn(Optional.of(account));

        Account result = accountUtils.findAccountByIbanOrThrow(accountRepository, iban);
        assertNotNull(result);

    }

    @Test
    void testFindAccountByIbanOrThrow_ibanNotFound() {

        String iban = "ES0000000000000000000000";
        when(accountRepository.findByIban(iban)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> accountUtils.findAccountByIbanOrThrow(accountRepository, iban)
        );

        assertEquals("Account with IBAN '" + iban + "' not found.", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatus());
        assertEquals(Constants.ERROR_IBAN_NOT_FOUND, exception.getErrorCode());
    }

}
