package com.shivam.beyourfundmanager.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shivam.beyourfundmanager.entity.Transaction;
import com.shivam.beyourfundmanager.domain.TransactionType;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /** All transactions of a user (ordered by timestamp for correct weighted-avg). */
    List<Transaction> findByUser_IdOrderByTimestampAsc(UUID userId);

    /** All transactions of a user for a specific stock. */
    List<Transaction> findByUser_IdAndStock_Symbol(UUID userId, String symbol);

    /** Filter by type (BUY or SELL). */
    List<Transaction> findByUser_IdAndStock_SymbolAndType(
            UUID userId,
            String symbol,
            TransactionType type
    );

    /** Ordered transactions (important for avg calculation). */
    List<Transaction> findByUser_IdAndStock_SymbolOrderByTimestampAsc(
            UUID userId,
            String symbol
    );
}
