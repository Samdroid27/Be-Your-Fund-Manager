package com.shivam.beyourfundmanager.controller;

import com.shivam.beyourfundmanager.dto.PortfolioSummaryResponse;
import com.shivam.beyourfundmanager.service.PortfolioService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

@Controller
public class PortfolioGraphQLController {

    private final PortfolioService portfolioService;

    public PortfolioGraphQLController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @QueryMapping
    public PortfolioSummaryResponse portfolio(@Argument Long userId) {
        return portfolioService.getPortfolio(userId);
    }
}
