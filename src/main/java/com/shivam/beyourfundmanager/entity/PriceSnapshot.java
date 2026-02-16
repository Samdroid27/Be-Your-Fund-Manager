package com.shivam.beyourfundmanager.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "uk_instrument_date",
            columnNames = {"instrument_id", "price_date"})
})
public class PriceSnapshot extends BaseEntity {

    @ManyToOne(optional = false)
    private Instrument instrument;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDate priceDate;
}
