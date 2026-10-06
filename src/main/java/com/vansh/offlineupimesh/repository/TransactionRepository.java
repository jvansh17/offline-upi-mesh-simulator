package com.vansh.offlineupimesh.repository;

import com.vansh.offlineupimesh.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderAccountNumberOrReceiverAccountNumberOrderByTimestampDesc(
            String senderAccountNumber,
            String receiverAccountNumber
    );
}