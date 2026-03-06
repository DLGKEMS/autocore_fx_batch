package com.hakjin.autocore.processing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class KrwConvertedRow {
    String requestId;
    LocalDate postingDate;
    String vendorCode;
    String description;
    String currency;
    BigDecimal amount;
    BigDecimal rateToKrw;
    BigDecimal amountKrw;

    public KrwConvertedRow(String requestId, LocalDate postingDate, String vendorCode, String description, String currency, BigDecimal amount, BigDecimal rateToKrw, BigDecimal amountKrw) {
        this.requestId = requestId;
        this.postingDate = postingDate;
        this.vendorCode = vendorCode;
        this.description = description;
        this.currency = currency;
        this.amount = amount;
        this.rateToKrw = rateToKrw;
        this.amountKrw = amountKrw;
    }

    public String getRequestId() {
        return requestId;
    }

    public LocalDate getPostingDate() {
        return postingDate;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getRateToKrw() {
        return rateToKrw;
    }

    public BigDecimal getAmountKrw() {
        return amountKrw;
    }

}
