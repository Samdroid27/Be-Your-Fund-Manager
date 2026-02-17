package com.shivam.beyourfundmanager.service;

import com.shivam.beyourfundmanager.dto.RealizedGainResponse;
import com.shivam.beyourfundmanager.entity.enums.TaxType;

import java.util.List;

public interface RealizedGainService {

    List<RealizedGainResponse> fetchGains(
            Long userId,
            TaxType taxType,
            String startDate,
            String endDate
    );
}
