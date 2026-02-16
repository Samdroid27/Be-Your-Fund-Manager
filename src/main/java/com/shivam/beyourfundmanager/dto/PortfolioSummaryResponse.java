package com.shivam.beyourfundmanager.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortfolioSummaryResponse {

    private BigDecimal totalInvestedAmount;

    private BigDecimal totalCurrentValue;

    private BigDecimal totalProfitLoss;

    private List<HoldingResponse> holdings;
}
