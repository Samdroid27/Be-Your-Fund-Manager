package com.shivam.beyourfundmanager.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shivam.beyourfundmanager.entity.Transaction;
import com.shivam.beyourfundmanager.entity.enums.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser_IdOrderByTransactionDateAsc(Long userId);

    @Query("""
        SELECT COALESCE(SUM(t.quantity * t.price), 0)
        FROM Transaction t
        WHERE t.user.id = :userId
        AND t.instrument.id = :instrumentId
        AND t.type = 'BUY'
    """)
    BigDecimal sumBuyValue(Long userId, Long instrumentId);

    @Query("""
        SELECT COALESCE(SUM(t.quantity), 0)
        FROM Transaction t
        WHERE t.user.id = :userId
        AND t.instrument.id = :instrumentId
        AND t.type = 'BUY'
    """)
    BigDecimal sumBuyQuantity(Long userId, Long instrumentId);

    List<Transaction> findByUser_IdAndType(
            Long userId,
            TransactionType type
    );
}
