package com.shivam.beyourfundmanager.domain;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {

    private final Map<String, Holding> holdings = new HashMap<>();

    public void applyTransaction(String symbol, TransactionType type, int quantity, double price) {
        Holding holding = holdings.computeIfAbsent(symbol, Holding::new);

        switch (type) {
            case BUY -> holding.applyBuy(quantity, price);
            case SELL -> holding.applySell(quantity);
        }
    }

    public Map<String, Holding> getHoldings() {
        return holdings;
    }
}
