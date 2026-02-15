package com.shivam.beyourfundmanager.service;

import com.shivam.beyourfundmanager.dto.CreateTransactionRequest;
import com.shivam.beyourfundmanager.dto.TransactionResponse;

import java.util.UUID;

public interface TransactionService {

    TransactionResponse createTransaction(UUID userId, CreateTransactionRequest request);
}
