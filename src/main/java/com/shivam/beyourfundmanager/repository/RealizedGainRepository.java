package com.shivam.beyourfundmanager.repository;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.shivam.beyourfundmanager.entity.RealizedGain;

public interface RealizedGainRepository extends JpaRepository<RealizedGain, Long> {

    @Query("""
        SELECT COALESCE(SUM(r.gainAmount), 0)
        FROM RealizedGain r
        WHERE r.user.id = :userId
        AND r.instrument.id = :instrumentId
    """)
    BigDecimal sumRealizedGain(UUID userId, Long instrumentId);

}
