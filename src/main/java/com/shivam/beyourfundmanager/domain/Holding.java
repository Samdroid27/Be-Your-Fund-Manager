package com.shivam.beyourfundmanager.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Holding {

    private final String stockSymbol;
    private int totalQuantity;
    private double averagePrice;

    public void applyBuy(int quantity, double price) {
        double totalInvestment = (averagePrice * totalQuantity) + (price * quantity);
        totalQuantity += quantity;
        averagePrice = totalInvestment / totalQuantity;
    }

    public void applySell(int quantity) {
        if (quantity > totalQuantity) {
            throw new IllegalArgumentException("Cannot sell more than holding");
        }
        totalQuantity -= quantity;
    }

    public double getInvestedAmount() {
        return totalQuantity * averagePrice;
    }
}
