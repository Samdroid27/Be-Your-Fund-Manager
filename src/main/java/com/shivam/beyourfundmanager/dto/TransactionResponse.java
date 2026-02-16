package com.shivam.beyourfundmanager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.shivam.beyourfundmanager.entity.enums.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {

    private Long id;

    private String symbol;

    private String exchange;

    private TransactionType type;

    private BigDecimal quantity;

    private BigDecimal price;

    private LocalDateTime transactionDate;
}
