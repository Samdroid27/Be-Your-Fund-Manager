package com.shivam.beyourfundmanager.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shivam.beyourfundmanager.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {

    Optional<Stock> findBySymbol(String symbol);

    boolean existsBySymbol(String symbol);
}