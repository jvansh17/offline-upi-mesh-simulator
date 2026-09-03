package com.vansh.offlineupimesh.dto;

import java.math.BigDecimal;

public class TransferRequest {
    private String receiverAccountNumber;
    private BigDecimal amount;
    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }
    public void setAmount(BigDecimal amount){
        this.amount= amount;
    }
    public String getReceiverAccountNumber(){
        return receiverAccountNumber;
    }
    public BigDecimal getAmount(){
        return amount;
    }

}
