package com.shivam.beyourfundmanager.service.impl;

import com.shivam.beyourfundmanager.dto.RealizedGainResponse;
import com.shivam.beyourfundmanager.entity.RealizedGain;
import com.shivam.beyourfundmanager.entity.enums.TaxType;
import com.shivam.beyourfundmanager.repository.RealizedGainRepository;
import com.shivam.beyourfundmanager.service.RealizedGainService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RealizedGainServiceImpl implements RealizedGainService {

    private final RealizedGainRepository realizedGainRepository;

    public RealizedGainServiceImpl(
            RealizedGainRepository realizedGainRepository
    ) {
        this.realizedGainRepository = realizedGainRepository;
    }

    @Override
    public List<RealizedGainResponse> fetchGains(
            Long userId,
            TaxType taxType,
            String startDate,
            String endDate
    ) {

        List<RealizedGain> gains =
                realizedGainRepository.findByUser_Id(userId);

        return gains.stream()
                .filter(g -> filterByDate(g, startDate, endDate))
                .map(this::mapToResponse)
                .filter(r -> taxType == null || r.getTaxType() == taxType)
                .collect(Collectors.toList());
    }

    private boolean filterByDate(
            RealizedGain gain,
            String startDate,
            String endDate
    ) {

        if (startDate == null && endDate == null) {
            return true;
        }

        LocalDate sellDate = gain.getSellDate().toLocalDate();

        if (startDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            if (sellDate.isBefore(start)) return false;
        }

        if (endDate != null) {
            LocalDate end = LocalDate.parse(endDate);
            if (sellDate.isAfter(end)) return false;
        }

        return true;
    }

    private RealizedGainResponse mapToResponse(
            RealizedGain gain
    ) {

        RealizedGainResponse response =
                new RealizedGainResponse();

        response.setId(gain.getId());
        response.setInstrumentId(
                gain.getInstrument().getId()
        );
        response.setSymbol(
                gain.getInstrument().getSymbol()
        );
        response.setQuantity(gain.getQuantity());
        response.setBuyPrice(gain.getBuyPrice());
        response.setSellPrice(gain.getSellPrice());
        response.setGainAmount(gain.getGainAmount());
        response.setBuyDate(gain.getBuyDate());
        response.setSellDate(gain.getSellDate());

        response.setTaxType(
                classifyTax(gain)
        );

        return response;
    }

    // ------------------------------
    // STCG vs LTCG Logic
    // ------------------------------

    private TaxType classifyTax(
            RealizedGain gain
    ) {

        long holdingDays =
                ChronoUnit.DAYS.between(
                        gain.getBuyDate(),
                        gain.getSellDate()
                );

        // Indian equity rule: > 365 days = LTCG
        if (holdingDays > 365) {
            return TaxType.LTCG;
        }

        return TaxType.STCG;
    }
}
