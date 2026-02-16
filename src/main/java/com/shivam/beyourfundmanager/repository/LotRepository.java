package com.shivam.beyourfundmanager.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shivam.beyourfundmanager.entity.Lot;

public interface LotRepository extends JpaRepository<Lot, Long> {

    @Query("""
        SELECT l
        FROM Lot l
        WHERE l.user.id = :userId
        AND l.instrument.id = :instrumentId
        AND l.remainingQuantity > 0
        ORDER BY l.buyDate ASC
    """)
    List<Lot> findActiveLotsForFIFO(Long userId, Long instrumentId);

    List<Lot> findByUser_IdAndRemainingQuantityGreaterThan(
            UUID userId,
            BigDecimal quantity
    );
}
