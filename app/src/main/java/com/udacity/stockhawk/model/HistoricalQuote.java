package com.udacity.stockhawk.model;

import java.math.BigDecimal;

public class HistoricalQuote {

    private long timestamp;
    private BigDecimal price;

    public HistoricalQuote(long timestamp, BigDecimal price) {
        this.timestamp = timestamp;
        this.price = price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "HistoricalQuote{" +
                "timestamp=" + timestamp +
                ", price=" + price +
                '}';
    }
}
