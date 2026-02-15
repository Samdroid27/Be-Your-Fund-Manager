package com.shivam.beyourfundmanager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shivam.beyourfundmanager.entity.Transaction;
import com.shivam.beyourfundmanager.domain.TransactionType;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // All transactions of a user
    List<Transaction> findByUserId(UUID userId);

    // All transactions of a user for a specific stock
    List<Transaction> findByUserIdAndStockSymbol(UUID userId, String symbol);

    // Filter by type (BUY or SELL)
    List<Transaction> findByUserIdAndStockSymbolAndType(
            UUID userId,
            String symbol,
            TransactionType type
    );

    // Ordered transactions (important for avg calculation)
    List<Transaction> findByUserIdAndStockSymbolOrderByTimestampAsc(
            UUID userId,
            String symbol
    );
}
