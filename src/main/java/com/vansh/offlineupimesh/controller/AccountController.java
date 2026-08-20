package com.vansh.offlineupimesh.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.vansh.offlineupimesh.dto.CreateAccountRequest;
import com.vansh.offlineupimesh.dto.DepositRequest;
import com.vansh.offlineupimesh.entity.Account;
import com.vansh.offlineupimesh.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request.getName());
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/{accountNumber}/deposit")
    public Account depositMoney(
            @PathVariable("accountNumber") String accountNumber,
            @RequestBody DepositRequest request) {

        return accountService.depositMoney(
                accountNumber,
                request.getAmount()
        );
    }
}