package com.shivam.beyourfundmanager.service;

import com.shivam.beyourfundmanager.dto.PortfolioSummaryResponse;

import java.util.UUID;

public interface PortfolioService {

    PortfolioSummaryResponse getPortfolio(UUID userId);
}
