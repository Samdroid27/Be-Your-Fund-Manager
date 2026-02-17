package com.shivam.beyourfundmanager.dto;

import com.shivam.beyourfundmanager.entity.enums.TaxType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class RealizedGainResponse {

    private Long id;

    private Long instrumentId;

    private String symbol;

    private BigDecimal quantity;

    private BigDecimal buyPrice;

    private BigDecimal sellPrice;

    private BigDecimal gainAmount;

    private LocalDateTime buyDate;

    private LocalDateTime sellDate;

    private TaxType taxType;
}
