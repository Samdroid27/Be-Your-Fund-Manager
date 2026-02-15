package com.shivam.beyourfundmanager.service.impl;

import com.shivam.beyourfundmanager.dto.CreateTransactionRequest;
import com.shivam.beyourfundmanager.dto.TransactionResponse;
import com.shivam.beyourfundmanager.entity.Stock;
import com.shivam.beyourfundmanager.entity.Transaction;
import com.shivam.beyourfundmanager.entity.User;
import com.shivam.beyourfundmanager.repository.StockRepository;
import com.shivam.beyourfundmanager.repository.TransactionRepository;
import com.shivam.beyourfundmanager.repository.UserRepository;
import com.shivam.beyourfundmanager.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  UserRepository userRepository,
                                  StockRepository stockRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public TransactionResponse createTransaction(UUID userId, CreateTransactionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Stock stock = stockRepository.findBySymbol(request.getStockSymbol())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setStock(stock);
        transaction.setType(request.getType());
        transaction.setQuantity(request.getQuantity());
        transaction.setPrice(request.getPrice());
        transaction.setTimestamp(LocalDateTime.now());

        Transaction saved = transactionRepository.save(transaction);

        TransactionResponse response = new TransactionResponse();
        response.setId(saved.getId());
        response.setStockSymbol(stock.getSymbol());
        response.setType(saved.getType());
        response.setQuantity(saved.getQuantity());
        response.setPrice(saved.getPrice());
        response.setTimestamp(saved.getTimestamp());

        return response;
    }
}
