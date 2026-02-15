package com.shivam.beyourfundmanager.dto;

import lombok.Data;

@Data
public class HoldingResponse {

    private String stockSymbol;
    private Integer totalQuantity;
    private Double averagePrice;
    private Double investedAmount;
    private Double currentPrice;   // from external API later
    private Double currentValue;
    private Double profitLoss;     // calculated in service
    private Double profitLossPercentage;
}
