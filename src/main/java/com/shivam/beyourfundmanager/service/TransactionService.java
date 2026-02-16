package com.shivam.beyourfundmanager.service;

import com.shivam.beyourfundmanager.dto.CreateTransactionRequest;
import com.shivam.beyourfundmanager.dto.TransactionResponse;

public interface TransactionService {

    TransactionResponse createTransaction(CreateTransactionRequest request);
}
