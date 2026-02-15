package com.shivam.beyourfundmanager.dto;

import lombok.Data;

import java.util.List;

@Data
public class PortfolioSummaryResponse {

    private Double totalInvestedAmount;
    private Double totalCurrentValue;
    private Double totalProfitLoss;
    private Double totalProfitLossPercentage;

    private List<HoldingResponse> holdings;
}
