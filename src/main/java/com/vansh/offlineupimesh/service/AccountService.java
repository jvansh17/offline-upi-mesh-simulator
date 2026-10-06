package com.vansh.offlineupimesh.service;

import com.vansh.offlineupimesh.entity.Account;
import com.vansh.offlineupimesh.exception.AccountNotFoundException;
import com.vansh.offlineupimesh.exception.InvalidAmountException;
import com.vansh.offlineupimesh.exception.InsufficientBalanceException;
import com.vansh.offlineupimesh.exception.TransactionNotFoundException;
import com.vansh.offlineupimesh.repository.AccountRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.vansh.offlineupimesh.repository.TransactionRepository;
import com.vansh.offlineupimesh.entity.Transaction;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    public AccountService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
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

    public Account withdrawMoney(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Withdraw amount must be greater than zero");
        }
        if (amount.compareTo(account.getBalance()) > 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));

        Account updatedAccount = accountRepository.save(account);

        return updatedAccount;
    }

    @Transactional
    public Account transferMoney(
            String senderAccountNumber,
            String receiverAccountNumber,
            BigDecimal amount) {
        Account sender = accountRepository.findByAccountNumber(senderAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));
        Account receiver = accountRepository.findByAccountNumber(receiverAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Transfer amount must be greater than zero");
        }
        if (amount.compareTo(sender.getBalance()) > 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = new Transaction();

        transaction.setSenderAccountNumber(senderAccountNumber);
        transaction.setReceiverAccountNumber(receiverAccountNumber);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus("SUCCESS");
        transaction.setType("TRANSFER");

        transactionRepository.save(transaction);


        return sender;
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {

        return transactionRepository
                .findBySenderAccountNumberOrReceiverAccountNumberOrderByTimestampDesc(
                        accountNumber,
                        accountNumber
                );
    }

    public Transaction getTransactionById(Long id) {

        return transactionRepository.findById(id)
                .orElseThrow(() ->
                        new TransactionNotFoundException(
                                "Transaction not found"
                        )
                );
    }
}
