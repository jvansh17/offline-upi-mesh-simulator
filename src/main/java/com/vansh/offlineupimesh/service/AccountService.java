package com.vansh.offlineupimesh.service;

import com.vansh.offlineupimesh.entity.Account;
import com.vansh.offlineupimesh.exception.AccountNotFoundException;
import com.vansh.offlineupimesh.exception.InvalidAmountException;
import com.vansh.offlineupimesh.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    private String generateAccountNumber() {

        StringBuilder accountNumber = new StringBuilder();

        int firstDigit = secureRandom.nextInt(9) + 1;
        accountNumber.append(firstDigit);

        for (int i = 1; i < 12; i++) {
            int digit = secureRandom.nextInt(10);
            accountNumber.append(digit);
        }

        return accountNumber.toString();
    }

    public Account createAccount(String name) {
        String accountNumber = generateAccountNumber();

        while (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = generateAccountNumber();
        }

        Account account = new Account(name);
        account.setAccountNumber(accountNumber);

        Account savedAccount = accountRepository.save(account);

        return savedAccount;
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found"));
    }

    public Account depositMoney(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidAmountException("Deposit amount must be greater than zero");
        }
        account.setBalance(account.getBalance().add(amount));
        Account updatedAccount = accountRepository.save(account);
        return updatedAccount;

    }
}
