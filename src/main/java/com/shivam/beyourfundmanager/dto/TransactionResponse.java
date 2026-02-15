package com.shivam.beyourfundmanager.dto;

import com.shivam.beyourfundmanager.domain.TransactionType;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponse {

    private UUID id;
    private String stockSymbol;
    private TransactionType type;
    private Integer quantity;
    private Double price;
    private LocalDateTime timestamp;
}
