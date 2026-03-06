package com.hakjin.autocore.rate.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class DailyFxRates {
    private final Map<String, BigDecimal> rates;
    private final LocalDate postingDate;

    public DailyFxRates(Map<String, BigDecimal> rates, LocalDate postingDate) {
        this.rates = rates;
        this.postingDate = postingDate;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public LocalDate getPostingDate() {
        return postingDate;
    }
}
