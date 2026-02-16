package com.shivam.beyourfundmanager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.shivam.beyourfundmanager.entity.enums.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTransactionRequest {

    private Long userId;

    private String symbol;

    private String exchange; // null for MF/RSU

    private TransactionType type;

    private BigDecimal quantity;

    private BigDecimal price;

    private LocalDateTime transactionDate;
}
