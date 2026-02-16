package com.shivam.beyourfundmanager.service.impl;

import com.shivam.beyourfundmanager.dto.HoldingResponse;
import com.shivam.beyourfundmanager.dto.PortfolioSummaryResponse;
import com.shivam.beyourfundmanager.entity.Instrument;
import com.shivam.beyourfundmanager.entity.Lot;
import com.shivam.beyourfundmanager.repository.LotRepository;
import com.shivam.beyourfundmanager.repository.PriceSnapshotRepository;
import com.shivam.beyourfundmanager.repository.RealizedGainRepository;
import com.shivam.beyourfundmanager.repository.TransactionRepository;
import com.shivam.beyourfundmanager.service.PortfolioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private final LotRepository lotRepository;
    private final TransactionRepository transactionRepository;
    private final RealizedGainRepository realizedGainRepository;
    private final PriceSnapshotRepository priceSnapshotRepository;

    public PortfolioServiceImpl(
            LotRepository lotRepository,
            TransactionRepository transactionRepository,
            RealizedGainRepository realizedGainRepository,
            PriceSnapshotRepository priceSnapshotRepository
    ) {
        this.lotRepository = lotRepository;
        this.transactionRepository = transactionRepository;
        this.realizedGainRepository = realizedGainRepository;
        this.priceSnapshotRepository = priceSnapshotRepository;
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public PortfolioSummaryResponse getPortfolio(Long userId) {

        List<Lot> activeLots = fetchActiveLots(userId);
        Map<Instrument, List<Lot>> groupedLots = groupLotsByInstrument(activeLots);

        List<HoldingResponse> holdings = new ArrayList<>();
        BigDecimal totalInvested = BigDecimal.ZERO;
        BigDecimal totalCurrentValue = BigDecimal.ZERO;

        for (Map.Entry<Instrument, List<Lot>> entry : groupedLots.entrySet()) {

            Instrument instrument = entry.getKey();
            List<Lot> lots = entry.getValue();

            HoldingResponse holding = buildHolding(userId, instrument, lots);
            holdings.add(holding);

            totalInvested = totalInvested.add(
                    holding.getWeightedAverage()
                            .multiply(holding.getQuantity())
            );

            totalCurrentValue = totalCurrentValue.add(
                    holding.getCurrentValue()
            );
        }

        return buildPortfolioSummary(holdings, totalInvested, totalCurrentValue);
    }

    // ------------------------------
    // Modular Helper Functions
    // ------------------------------

    private List<Lot> fetchActiveLots(Long userId) {
        return lotRepository.findByUser_IdAndRemainingQuantityGreaterThan(
                userId, BigDecimal.ZERO
        );
    }

    private Map<Instrument, List<Lot>> groupLotsByInstrument(List<Lot> lots) {
        Map<Instrument, List<Lot>> map = new HashMap<>();
        for (Lot lot : lots) {
            map.computeIfAbsent(lot.getInstrument(), k -> new ArrayList<>())
               .add(lot);
        }
        return map;
    }

    private HoldingResponse buildHolding(Long userId,
                                         Instrument instrument,
                                         List<Lot> lots) {

        BigDecimal totalQty = calculateTotalQuantity(lots);
        BigDecimal weightedCost = calculateWeightedCost(lots);

        BigDecimal weightedAverage =
                safeDivide(weightedCost, totalQty);

        BigDecimal buyOnlyAverage =
                calculateBuyOnlyAverage(userId, instrument.getId());

        BigDecimal capitalAdjustedAverage =
                calculateCapitalAdjustedAverage(userId, instrument.getId(), totalQty);

        BigDecimal currentPrice =
                fetchLatestPrice(instrument.getId(), weightedAverage);

        BigDecimal currentValue =
                totalQty.multiply(currentPrice);

        BigDecimal profitLoss =
                currentValue.subtract(weightedCost);

        HoldingResponse response = new HoldingResponse();
        response.setSymbol(instrument.getSymbol());
        response.setQuantity(totalQty);
        response.setWeightedAverage(weightedAverage);
        response.setBuyOnlyAverage(buyOnlyAverage);
        response.setCapitalAdjustedAverage(capitalAdjustedAverage);
        response.setCurrentPrice(currentPrice);
        response.setCurrentValue(currentValue);
        response.setProfitLoss(profitLoss);

        return response;
    }

    private BigDecimal calculateTotalQuantity(List<Lot> lots) {
        return lots.stream()
                .map(Lot::getRemainingQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateWeightedCost(List<Lot> lots) {
        return lots.stream()
                .map(l -> l.getRemainingQuantity().multiply(l.getBuyPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateBuyOnlyAverage(Long userId, Long instrumentId) {
        BigDecimal totalBuyValue = Optional.ofNullable(
                transactionRepository.sumBuyValue(userId, instrumentId)
        ).orElse(BigDecimal.ZERO);

        BigDecimal totalBuyQty = Optional.ofNullable(
                transactionRepository.sumBuyQuantity(userId, instrumentId)
        ).orElse(BigDecimal.ZERO);

        return safeDivide(totalBuyValue, totalBuyQty);
    }

    private BigDecimal calculateCapitalAdjustedAverage(Long userId,
                                                       Long instrumentId,
                                                       BigDecimal totalQty) {

        BigDecimal totalBuyValue = Optional.ofNullable(
                transactionRepository.sumBuyValue(userId, instrumentId)
        ).orElse(BigDecimal.ZERO);

        BigDecimal realizedGain = Optional.ofNullable(
                realizedGainRepository.sumRealizedGain(userId, instrumentId)
        ).orElse(BigDecimal.ZERO);

        BigDecimal netInvested = totalBuyValue.subtract(realizedGain);

        return safeDivide(netInvested, totalQty);
    }

    private BigDecimal fetchLatestPrice(Long instrumentId,
                                        BigDecimal fallbackPrice) {

        return Optional.ofNullable(
                priceSnapshotRepository.findLatestPrice(instrumentId)
        ).orElse(fallbackPrice);
    }

    private BigDecimal safeDivide(BigDecimal numerator,
                                  BigDecimal denominator) {

        if (denominator == null ||
                denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return numerator.divide(denominator, 6, RoundingMode.HALF_UP);
    }

    private PortfolioSummaryResponse buildPortfolioSummary(
            List<HoldingResponse> holdings,
            BigDecimal totalInvested,
            BigDecimal totalCurrentValue) {

        PortfolioSummaryResponse summary =
                new PortfolioSummaryResponse();

        summary.setHoldings(holdings);
        summary.setTotalInvestedAmount(totalInvested);
        summary.setTotalCurrentValue(totalCurrentValue);
        summary.setTotalProfitLoss(
                totalCurrentValue.subtract(totalInvested)
        );

        return summary;
    }
}
