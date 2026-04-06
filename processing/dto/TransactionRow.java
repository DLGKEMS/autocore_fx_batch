package com.hakjin.autocore.processing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionRow {
    String requestId; // 거래 요청 식별용 id
    LocalDate postingDate; // 회계 전표 기준일
    String vendorCode; // 거래처 식별 코드
    String description; // 거래 내용에 대한 설명
    String currency; // 통화 코드
    BigDecimal amount; // 거래 금액

    public TransactionRow(String requestId, LocalDate postingDate, String vendorCode, String description, String currency, BigDecimal amount) {
        this.requestId = requestId;
        this.postingDate = postingDate;
        this.vendorCode = vendorCode;
        this.description = description;
        this.currency = currency;
        this.amount = amount;
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

}
