package com.vansh.offlineupimesh.controller;

import com.vansh.offlineupimesh.entity.Transaction;
import com.vansh.offlineupimesh.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final AccountService accountService;

    public TransactionController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return accountService.getTransactionById(id);
    }

    @GetMapping("/account/{accountNumber}")
    public List<Transaction> getTransactionHistory(
            @PathVariable("accountNumber") String accountNumber) {

        return accountService.getTransactionHistory(accountNumber);
    }
}