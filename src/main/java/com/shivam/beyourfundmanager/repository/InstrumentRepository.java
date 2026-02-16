package com.shivam.beyourfundmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shivam.beyourfundmanager.entity.Instrument;

public interface InstrumentRepository extends JpaRepository<Instrument, Long> {

    Optional<Instrument> findBySymbolAndExchange(String symbol, String exchange);

}
