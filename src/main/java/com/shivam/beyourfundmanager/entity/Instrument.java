package com.shivam.beyourfundmanager.entity;

import com.shivam.beyourfundmanager.entity.enums.AssetType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "symbol")
})
public class Instrument extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String symbol; // TCS, HDFC, INFY, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType assetType;

    @Column(nullable = true)
    private String exchange; // NSE/BSE (null for MF)

    @Column(nullable = true)
    private String isin;
}


