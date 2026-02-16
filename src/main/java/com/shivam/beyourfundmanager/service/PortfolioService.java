package com.shivam.beyourfundmanager.service;

import com.shivam.beyourfundmanager.dto.PortfolioSummaryResponse;

public interface PortfolioService {

    PortfolioSummaryResponse getPortfolio(Long userId);
}
