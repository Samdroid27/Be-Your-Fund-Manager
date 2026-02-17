package com.shivam.beyourfundmanager.service;

import com.shivam.beyourfundmanager.dto.PortfolioSummaryResponse;
import com.shivam.beyourfundmanager.entity.enums.PortfolioViewType;

public interface PortfolioService {

    PortfolioSummaryResponse getPortfolio(
        Long userId,
        PortfolioViewType viewType
);

}
