package com.banking.account_service.utils;

import com.banking.account_service.entities.Account;
import com.banking.account_service.error.CustomException;
import com.banking.account_service.repositories.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.money.Monetary;
import javax.money.UnknownCurrencyException;

@Component
public class AccountUtils {

    /**
     * Finds an account by its IBAN and returns it. If the account is not found,
     * a {@link CustomException} is thrown with a detailed error message.
     *
     * @param iban the IBAN of the account to find
     * @return the {@link Account} associated with the given IBAN
     * @throws CustomException if no account with the given IBAN is found
     */
    public Account findAccountByIbanOrThrow(AccountRepository accountRepository, String iban) {
        return accountRepository.findByIban(iban)
                .orElseThrow(() -> new CustomException(
                        "Account with IBAN '" + iban + "' not found.",              // Message
                        HttpStatus.NOT_FOUND.value(),                               // HTTP Status Code
                        Constants.ERROR_IBAN_NOT_FOUND// Error Code
                ));
    }

    /**
     * Validates if the provided currency code corresponds to a valid ISO 4217 currency.
     *
     * @param currencyCode the ISO 4217 currency code to be validated
     * @throws CustomException if the currency code is not recognized or valid
     */
    public void validateCurrency(String currencyCode) {
        try {
            Monetary.getCurrency(currencyCode);
        } catch (UnknownCurrencyException e) {
            throw new CustomException(
                    "Currency '" + currencyCode + "' not found.",
                    HttpStatus.NOT_FOUND.value(),
                    Constants.ERROR_CURRENCY_NOT_FOUND
            );
        }
    }
}
