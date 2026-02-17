package com.shivam.beyourfundmanager.controller;

import com.shivam.beyourfundmanager.entity.Instrument;
import com.shivam.beyourfundmanager.repository.InstrumentRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class InstrumentGraphQLController {

    private final InstrumentRepository instrumentRepository;

    public InstrumentGraphQLController(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    @QueryMapping
    public List<Instrument> instruments() {
        return instrumentRepository.findAll();
    }

    @QueryMapping
    public Instrument instrument(@Argument Long id) {
        return instrumentRepository.findById(id).orElse(null);
    }
}
