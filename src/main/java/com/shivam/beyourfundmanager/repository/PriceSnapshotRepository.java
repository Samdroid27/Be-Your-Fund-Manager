package com.shivam.beyourfundmanager.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shivam.beyourfundmanager.entity.PriceSnapshot;

public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {

    @Query("""
        SELECT p.price
        FROM PriceSnapshot p
        WHERE p.instrument.id = :instrumentId
        ORDER BY p.priceDate DESC
        LIMIT 1
    """)
    BigDecimal findLatestPrice(Long instrumentId);

}
