package com.shivam.beyourfundmanager.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;

class HoldingTest {

    @Nested
    @DisplayName("constructor and getters")
    class ConstructorAndGetters {

        // Example: symbol=RELIANCE → getStockSymbol=RELIANCE, qty=0, avgPrice=0.
        @Test
        @DisplayName("creates holding with stock symbol")
        void createsWithSymbol() {
            Holding holding = new Holding("RELIANCE");
            assertThat(holding.getStockSymbol()).isEqualTo("RELIANCE");
            assertThat(holding.getTotalQuantity()).isZero();
            assertThat(holding.getAveragePrice()).isZero();
        }
    }

    @Nested
    @DisplayName("applyBuy")
    class ApplyBuy {

        // Scenario: First BUY — TCS applyBuy(10, 3500) → qty=10, avgPrice=3500, invested=35_000.
        @Test
        @DisplayName("first buy sets quantity and average price")
        void firstBuySetsQuantityAndAveragePrice() {
            Holding holding = new Holding("TCS");
            holding.applyBuy(10, 3500.0);

            assertThat(holding.getTotalQuantity()).isEqualTo(10);
            assertThat(holding.getAveragePrice()).isEqualTo(3500.0);
            assertThat(holding.getInvestedAmount()).isEqualTo(35_000.0);
        }

        // Scenario: Second BUY — RELIANCE applyBuy(10, 2500) then applyBuy(5, 2600) → qty=15, avg≈2533.33, invested=38_000.
        @Test
        @DisplayName("second buy updates volume-weighted average price")
        void secondBuyUpdatesAveragePrice() {
            Holding holding = new Holding("RELIANCE");
            holding.applyBuy(10, 2500.0);  // 10 @ 2500
            holding.applyBuy(5, 2600.0);   // 15 total, avg = (25000 + 13000) / 15 = 2533.33...

            assertThat(holding.getTotalQuantity()).isEqualTo(15);
            assertThat(holding.getAveragePrice()).isCloseTo(2533.333, offset(0.01));
            assertThat(holding.getInvestedAmount()).isCloseTo(38_000.0, offset(0.01));
        }
    }

    @Nested
    @DisplayName("applySell")
    class ApplySell {

        // Scenario: SELL partial — TCS BUY 10@3500 then SELL 3 → qty=7, avgPrice=3500 unchanged.
        @Test
        @DisplayName("reduces quantity")
        void reducesQuantity() {
            Holding holding = new Holding("TCS");
            holding.applyBuy(10, 3500.0);
            holding.applySell(3);

            assertThat(holding.getTotalQuantity()).isEqualTo(7);
            assertThat(holding.getAveragePrice()).isEqualTo(3500.0);
        }

        // Scenario: SELL all — TCS BUY 5@100 then SELL 5 → qty=0, avgPrice=100.
        @Test
        @DisplayName("selling all leaves zero quantity")
        void sellingAllLeavesZero() {
            Holding holding = new Holding("TCS");
            holding.applyBuy(5, 100.0);
            holding.applySell(5);

            assertThat(holding.getTotalQuantity()).isZero();
            assertThat(holding.getAveragePrice()).isEqualTo(100.0);
        }

        // Scenario: SELL over — TCS BUY 5@100 then SELL 10 → throws (cannot sell more than 5).
        @Test
        @DisplayName("throws when selling more than holding")
        void throwsWhenSellingMoreThanHolding() {
            Holding holding = new Holding("TCS");
            holding.applyBuy(5, 100.0);

            assertThatThrownBy(() -> holding.applySell(10))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot sell more than holding");
        }
    }

    @Nested
    @DisplayName("getInvestedAmount")
    class GetInvestedAmount {

        // Example: TCS applyBuy(4, 2500) → getInvestedAmount = 4 × 2500 = 10_000.
        @Test
        @DisplayName("returns quantity times average price")
        void returnsQuantityTimesAveragePrice() {
            Holding holding = new Holding("TCS");
            holding.applyBuy(4, 2500.0);

            assertThat(holding.getInvestedAmount()).isEqualTo(10_000.0);
        }
    }
}
