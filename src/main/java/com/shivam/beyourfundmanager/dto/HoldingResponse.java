package com.shivam.beyourfundmanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class HoldingResponse {

    private Long instrumentId;

    private String symbol;

    private BigDecimal quantity;

    // Selected view average
    private BigDecimal averagePrice;

    private BigDecimal currentPrice;

    private BigDecimal currentValue;

    private BigDecimal profitLoss;
}
