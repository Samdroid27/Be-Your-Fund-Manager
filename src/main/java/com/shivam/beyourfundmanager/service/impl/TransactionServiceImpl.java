package com.shivam.beyourfundmanager.service.impl;

import com.shivam.beyourfundmanager.dto.CreateTransactionRequest;
import com.shivam.beyourfundmanager.dto.TransactionResponse;
import com.shivam.beyourfundmanager.entity.*;
import com.shivam.beyourfundmanager.entity.enums.TransactionType;
import com.shivam.beyourfundmanager.repository.*;
import com.shivam.beyourfundmanager.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepository;
    private final InstrumentRepository instrumentRepository;
    private final TransactionRepository transactionRepository;
    private final LotRepository lotRepository;
    private final RealizedGainRepository realizedGainRepository;

    public TransactionServiceImpl(
            UserRepository userRepository,
            InstrumentRepository instrumentRepository,
            TransactionRepository transactionRepository,
            LotRepository lotRepository,
            RealizedGainRepository realizedGainRepository
    ) {
        this.userRepository = userRepository;
        this.instrumentRepository = instrumentRepository;
        this.transactionRepository = transactionRepository;
        this.lotRepository = lotRepository;
        this.realizedGainRepository = realizedGainRepository;
    }

    @Override
    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request) {

        User user = fetchUser(request.getUserId());
        Instrument instrument = fetchInstrument(
                request.getSymbol(),
                request.getExchange()
        );

        Transaction transaction = buildTransaction(user, instrument, request);
        transactionRepository.save(transaction);

        if (request.getType() == TransactionType.BUY ||
                request.getType() == TransactionType.RSU_VEST) {

            createLot(user, instrument, transaction);

        } else if (request.getType() == TransactionType.SELL) {

            processSell(user, instrument, transaction);
        }

        return buildResponse(transaction);
    }

    // ------------------------------
    // Core Logic
    // ------------------------------

    private void createLot(User user,
                           Instrument instrument,
                           Transaction transaction) {

        Lot lot = new Lot();
        lot.setUser(user);
        lot.setInstrument(instrument);
        lot.setOriginalQuantity(transaction.getQuantity());
        lot.setRemainingQuantity(transaction.getQuantity());
        lot.setBuyPrice(transaction.getPrice());
        lot.setBuyDate(transaction.getTransactionDate());
        lot.setTransaction(transaction);

        lotRepository.save(lot);
    }

    private void processSell(User user,
                             Instrument instrument,
                             Transaction sellTransaction) {

        BigDecimal quantityToSell = sellTransaction.getQuantity();

        List<Lot> activeLots =
                lotRepository.findActiveLotsForFIFO(
                        user.getId(),
                        instrument.getId()
                );

        for (Lot lot : activeLots) {

            if (quantityToSell.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal lotRemaining = lot.getRemainingQuantity();

            BigDecimal quantityConsumed =
                    lotRemaining.min(quantityToSell);

            generateRealizedGain(
                    user,
                    instrument,
                    lot,
                    sellTransaction,
                    quantityConsumed
            );

            lot.setRemainingQuantity(
                    lotRemaining.subtract(quantityConsumed)
            );

            lotRepository.save(lot);

            quantityToSell =
                    quantityToSell.subtract(quantityConsumed);
        }

        if (quantityToSell.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Not enough quantity to sell");
        }
    }

    private void generateRealizedGain(User user,
                                      Instrument instrument,
                                      Lot lot,
                                      Transaction sellTransaction,
                                      BigDecimal quantityConsumed) {

        BigDecimal gainPerUnit =
                sellTransaction.getPrice()
                        .subtract(lot.getBuyPrice());

        BigDecimal totalGain =
                gainPerUnit.multiply(quantityConsumed)
                        .setScale(4, RoundingMode.HALF_UP);

        RealizedGain gain = new RealizedGain();
        gain.setUser(user);
        gain.setInstrument(instrument);
        gain.setQuantity(quantityConsumed);
        gain.setBuyPrice(lot.getBuyPrice());
        gain.setSellPrice(sellTransaction.getPrice());
        gain.setGainAmount(totalGain);
        gain.setBuyDate(lot.getBuyDate());
        gain.setSellDate(sellTransaction.getTransactionDate());

        realizedGainRepository.save(gain);
    }

    // ------------------------------
    // Builders & Fetchers
    // ------------------------------

    private User fetchUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));
    }

    private Instrument fetchInstrument(String symbol,
                                       String exchange) {

        return instrumentRepository
                .findBySymbolAndExchange(symbol, exchange)
                .orElseThrow(() ->
                        new IllegalArgumentException("Instrument not found"));
    }

    private Transaction buildTransaction(User user,
                                         Instrument instrument,
                                         CreateTransactionRequest request) {

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setInstrument(instrument);
        transaction.setType(request.getType());
        transaction.setQuantity(request.getQuantity());
        transaction.setPrice(request.getPrice());
        transaction.setTransactionDate(request.getTransactionDate());

        return transaction;
    }

    private TransactionResponse buildResponse(Transaction transaction) {

        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setSymbol(transaction.getInstrument().getSymbol());
        response.setExchange(transaction.getInstrument().getExchange());
        response.setType(transaction.getType());
        response.setQuantity(transaction.getQuantity());
        response.setPrice(transaction.getPrice());
        response.setTransactionDate(transaction.getTransactionDate());

        return response;
    }
}
