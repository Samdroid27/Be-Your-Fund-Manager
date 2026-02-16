package com.shivam.beyourfundmanager.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(name = "idx_lot_user_instrument", columnList = "user_id,instrument_id")
})
public class Lot extends BaseEntity {

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Instrument instrument;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal originalQuantity;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal remainingQuantity;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal buyPrice;

    @Column(nullable = false)
    private LocalDateTime buyDate;

    @ManyToOne(optional = false)
    private Transaction transaction;
}
