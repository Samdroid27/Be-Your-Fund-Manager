package com.shivam.beyourfundmanager.controller;

import com.shivam.beyourfundmanager.dto.CreateTransactionRequest;
import com.shivam.beyourfundmanager.dto.TransactionResponse;
import com.shivam.beyourfundmanager.service.TransactionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TransactionGraphQLController {

    private final TransactionService transactionService;

    public TransactionGraphQLController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @MutationMapping
    public TransactionResponse createTransaction(@Argument CreateTransactionRequest input) {
        return transactionService.createTransaction(input);
    }
}
