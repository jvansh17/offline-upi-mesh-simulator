package com.vansh.offlineupimesh.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    // Constructors

    public Account(){

    }

    public Account(String name) {
        this.name = name;
    }

    // Getters

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }


    // Setters

    public void setName(String name){
        this.name = name;
    }

    public void setAccountNumber(String accountNumber){
        this.accountNumber = accountNumber;
    }

    // toString()
}