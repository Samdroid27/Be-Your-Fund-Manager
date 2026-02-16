package com.shivam.beyourfundmanager.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HoldingResponse {

    private String symbol;

    private BigDecimal quantity;

    private BigDecimal weightedAverage;

    private BigDecimal buyOnlyAverage;

    private BigDecimal capitalAdjustedAverage;

    private BigDecimal currentPrice;

    private BigDecimal currentValue;

    private BigDecimal profitLoss;
}
