package com.shivam.beyourfundmanager.service.impl;

import com.shivam.beyourfundmanager.domain.Holding;
import com.shivam.beyourfundmanager.domain.Portfolio;
import com.shivam.beyourfundmanager.dto.HoldingResponse;
import com.shivam.beyourfundmanager.dto.PortfolioSummaryResponse;
import com.shivam.beyourfundmanager.entity.Transaction;
import com.shivam.beyourfundmanager.repository.TransactionRepository;
import com.shivam.beyourfundmanager.service.PortfolioService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final TransactionRepository transactionRepository;

    public PortfolioServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public PortfolioSummaryResponse getPortfolio(UUID userId) {
        List<Transaction> transactions =
                transactionRepository.findByUser_IdOrderByTimestampAsc(userId);

        Portfolio portfolio = new Portfolio();

        for (Transaction t : transactions) {
            portfolio.applyTransaction(
                    t.getStock().getSymbol(),
                    t.getType(),
                    t.getQuantity(),
                    t.getPrice()
            );
        }

        List<HoldingResponse> holdingResponses = new ArrayList<>();
        double totalInvested = 0.0;
        double totalCurrentValue = 0.0;

        for (Holding holding : portfolio.getHoldings().values()) {
            double currentPrice = holding.getAveragePrice();
            // TODO: replace with real market API later

            double investedAmount = holding.getInvestedAmount();
            double currentValue = holding.getTotalQuantity() * currentPrice;
            double profitLoss = currentValue - investedAmount;

            HoldingResponse response = new HoldingResponse();
            response.setStockSymbol(holding.getStockSymbol());
            response.setTotalQuantity(holding.getTotalQuantity());
            response.setAveragePrice(holding.getAveragePrice());
            response.setInvestedAmount(investedAmount);
            response.setCurrentPrice(currentPrice);
            response.setCurrentValue(currentValue);
            response.setProfitLoss(profitLoss);
            response.setProfitLossPercentage(
                    investedAmount == 0 ? 0 : (profitLoss / investedAmount) * 100
            );

            holdingResponses.add(response);
            totalInvested += investedAmount;
            totalCurrentValue += currentValue;
        }

        double totalProfitLoss = totalCurrentValue - totalInvested;

        PortfolioSummaryResponse summary = new PortfolioSummaryResponse();
        summary.setTotalInvestedAmount(totalInvested);
        summary.setTotalCurrentValue(totalCurrentValue);
        summary.setTotalProfitLoss(totalProfitLoss);
        summary.setTotalProfitLossPercentage(
                totalInvested == 0 ? 0 : (totalProfitLoss / totalInvested) * 100
        );
        summary.setHoldings(holdingResponses);

        return summary;
    }
}
