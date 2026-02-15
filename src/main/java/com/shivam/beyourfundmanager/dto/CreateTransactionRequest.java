package com.shivam.beyourfundmanager.dto;

import com.shivam.beyourfundmanager.domain.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateTransactionRequest {

    @NotBlank(message = "Stock symbol is required")
    private String stockSymbol;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;
}
