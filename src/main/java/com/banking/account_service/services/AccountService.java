package com.banking.account_service.services;

import com.banking.account_service.dto.AccountBalanceResponse;
import com.banking.account_service.entities.Account;
import com.banking.account_service.error.CustomException;
import com.banking.account_service.repositories.AccountRepository;
import com.banking.account_service.utils.AccountUtils;
import com.banking.account_service.utils.Constants;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountUtils accountUtils;

    public AccountService(AccountRepository accountRepository, AccountUtils accountUtils) {
        this.accountRepository = accountRepository;
        this.accountUtils = accountUtils;
    }

    /**
     * Retrieves the account balance details for the given IBAN.
     *
     * @param iban the International Bank Account Number (IBAN) of the account whose balance is to be retrieved
     * @return an {@link AccountBalanceResponse} containing the IBAN and the associated balances
     * @throws CustomException if no account with the given IBAN is found
     */
    public AccountBalanceResponse getBalance(String iban) {
        Account account = accountUtils.findAccountByIbanOrThrow(accountRepository, iban);
        return new AccountBalanceResponse(account.getIban(), account.getBalances());
    }

    /**
     * Deposits the specified amount into the account identified by the given IBAN and currency.
     * Updates the account's balance for the specified currency and saves the updated account.
     *
     * @param iban     the International Bank Account Number (IBAN) for the account to deposit into
     * @param currency the currency in which the deposit is made
     * @param amount   the amount to be deposited into the account
     * @return the updated Account object with the new balance
     */
    @Transactional
    public Account deposit(String iban, String currency, BigDecimal amount) {

        Account account = accountUtils.findAccountByIbanOrThrow(accountRepository, iban);
        accountUtils.validateCurrency(currency);

        // Retrieve the current balance for the given currency in the account
        Map<String, BigDecimal> balances = account.getBalances();
        BigDecimal currentBalance = balances.getOrDefault(currency, BigDecimal.ZERO);

        // Add the deposit amount to the current balance
        balances.put(currency, currentBalance.add(amount));
        account.setBalances(balances);

        // Save the updated account
        return accountRepository.save(account);
    }

    /**
     * Deducts a specified amount from the balance of a given account and currency.
     * If the balance is insufficient for the requested deduction, an exception is thrown.
     *
     * @param iban     the International Bank Account Number (IBAN) of the account to be debited
     * @param currency the currency in which the debit operation will be performed
     * @param amount   the amount to be debited from the account
     * @return the updated {@link Account} after debiting the specified amount
     * @throws CustomException if there are insufficient funds in the specified currency
     */
    @Transactional
    public Account debit(String iban, String currency, BigDecimal amount) {

        Account account = accountUtils.findAccountByIbanOrThrow(accountRepository, iban);
        accountUtils.validateCurrency(currency);

        // Retrieve the current balance for the given currency in the account
        Map<String, BigDecimal> balances = account.getBalances();
        BigDecimal currentBalance = account.getBalances().getOrDefault(currency, BigDecimal.ZERO);

        // Verify if the account contains the specified currency
        if (!balances.containsKey(currency)) {
            throw new CustomException(
                    "The currency '" + currency + "' is not available for IBAN '" + iban + "'.",
                    HttpStatus.BAD_REQUEST.value(),
                    Constants.ERROR_INVALID_CURRENCY
            );
        }

        // Check if sufficient funds are available after subtracting the requested amount
        if (currentBalance.compareTo(amount) < 0) {
            throw new CustomException(
                    "Insufficient funds for IBAN '" + iban + "'. The withdrawal amount of " + amount + " exceeds the available balance of " + currentBalance + ".",
                    HttpStatus.UNPROCESSABLE_ENTITY.value(),
                    Constants.ERROR_INSUFFICIENT_FUNDS
            );
        }

        // Subtract the amount and update the balance
        BigDecimal updatedBalance = currentBalance.subtract(amount);
        balances.put(currency, updatedBalance);
        account.setBalances(balances);

        // Save the updated account to the database
        return accountRepository.save(account);
    }

}
