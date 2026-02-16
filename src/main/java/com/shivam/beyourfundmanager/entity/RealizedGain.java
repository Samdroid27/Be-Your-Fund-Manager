package com.shivam.beyourfundmanager.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
    indexes = {
        @Index(name = "idx_rg_user", columnList = "user_id"),
        @Index(name = "idx_rg_user_instr", columnList = "user_id,instrument_id"),
        @Index(name = "idx_rg_sell_date", columnList = "sell_date")
    }
)
public class RealizedGain extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "instrument_id", nullable = false)
    private Instrument instrument;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal buyPrice;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal sellPrice;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal gainAmount;

    @Column(nullable = false)
    private LocalDateTime buyDate;

    @Column(nullable = false)
    private LocalDateTime sellDate;
}
