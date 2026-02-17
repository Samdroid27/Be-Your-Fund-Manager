package com.shivam.beyourfundmanager.controller;

import com.shivam.beyourfundmanager.dto.RealizedGainResponse;
import com.shivam.beyourfundmanager.service.RealizedGainService;
import com.shivam.beyourfundmanager.entity.enums.TaxType;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RealizedGainGraphQLController {

    private final RealizedGainService realizedGainService;

    public RealizedGainGraphQLController(RealizedGainService realizedGainService) {
        this.realizedGainService = realizedGainService;
    }

    @QueryMapping
    public List<RealizedGainResponse> realizedGains(
            @Argument Long userId,
            @Argument TaxType taxType,
            @Argument String startDate,
            @Argument String endDate
    ) {
        return realizedGainService.fetchGains(
                userId,
                taxType,
                startDate,
                endDate
        );
    }
}
