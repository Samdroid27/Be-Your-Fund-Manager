package com.shivam.beyourfundmanager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.data.Offset.offset;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PortfolioTest {

    @Nested
    @DisplayName("getHoldings")
    class GetHoldings {

        @Test
        @DisplayName("is empty initially")
        void isEmptyInitially() {
            Portfolio portfolio = new Portfolio();
            assertThat(portfolio.getHoldings()).isEmpty();
        }
    }

    @Nested
    @DisplayName("applyTransaction BUY")
    class ApplyTransactionBuy {

        // Scenario: First BUY — symbol=RELIANCE, qty=10, price=2500 → holding: qty=10, avgPrice=2500.
        @Test
        @DisplayName("adds holding and updates quantity and average price")
        void addsHolding() {
            Portfolio portfolio = new Portfolio();
            portfolio.applyTransaction("RELIANCE", TransactionType.BUY, 10, 2500.0);

            Holding holding = portfolio.getHoldings().get("RELIANCE");
            assertThat(holding).isNotNull();
            assertThat(holding.getStockSymbol()).isEqualTo("RELIANCE");
            assertThat(holding.getTotalQuantity()).isEqualTo(10);
            assertThat(holding.getAveragePrice()).isEqualTo(2500.0);
        }

        // Scenario: Second BUY — TCS: BUY 10@3500 then BUY 5@3600 → qty=15, avgPrice≈3533.33.
        @Test
        @DisplayName("second BUY for same symbol updates existing holding")
        void secondBuyUpdatesHolding() {
            Portfolio portfolio = new Portfolio();
            portfolio.applyTransaction("TCS", TransactionType.BUY, 10, 3500.0);
            portfolio.applyTransaction("TCS", TransactionType.BUY, 5, 3600.0);

            Holding holding = portfolio.getHoldings().get("TCS");
            assertThat(holding.getTotalQuantity()).isEqualTo(15);
            assertThat(holding.getAveragePrice()).isCloseTo(3533.333, offset(0.01));
        }
    }

    @Nested
    @DisplayName("applyTransaction SELL")
    class ApplyTransactionSell {

        // Scenario: SELL partial — RELIANCE BUY 10@2500 then SELL 3 → qty=7, avgPrice=2500.
        @Test
        @DisplayName("reduces holding quantity")
        void reducesQuantity() {
            Portfolio portfolio = new Portfolio();
            portfolio.applyTransaction("RELIANCE", TransactionType.BUY, 10, 2500.0);
            portfolio.applyTransaction("RELIANCE", TransactionType.SELL, 3, 0);

            Holding holding = portfolio.getHoldings().get("RELIANCE");
            assertThat(holding.getTotalQuantity()).isEqualTo(7);
        }

        // Scenario: SELL over — TCS BUY 5@100 then SELL 10 → throws (cannot sell more than 5).
        @Test
        @DisplayName("throws when selling more than holding")
        void throwsWhenSellingMoreThanHolding() {
            Portfolio portfolio = new Portfolio();
            portfolio.applyTransaction("TCS", TransactionType.BUY, 5, 100.0);

            assertThatThrownBy(() ->
                    portfolio.applyTransaction("TCS", TransactionType.SELL, 10, 0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Cannot sell more than holding");
        }
    }

    @Nested
    @DisplayName("multiple symbols")
    class MultipleSymbols {

        // Scenario: Multiple symbols — RELIANCE 10@2500, TCS 5@3500 → 2 holdings, qty 10 and 5.
        @Test
        @DisplayName("keeps separate holdings per symbol")
        void keepsSeparateHoldings() {
            Portfolio portfolio = new Portfolio();
            portfolio.applyTransaction("RELIANCE", TransactionType.BUY, 10, 2500.0);
            portfolio.applyTransaction("TCS", TransactionType.BUY, 5, 3500.0);

            assertThat(portfolio.getHoldings()).hasSize(2);
            assertThat(portfolio.getHoldings().get("RELIANCE").getTotalQuantity()).isEqualTo(10);
            assertThat(portfolio.getHoldings().get("TCS").getTotalQuantity()).isEqualTo(5);
        }
    }
}
