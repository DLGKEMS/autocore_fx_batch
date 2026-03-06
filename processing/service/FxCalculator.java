package com.hakjin.autocore.processing.service;

import com.hakjin.autocore.processing.dto.KrwConvertedRow;
import com.hakjin.autocore.processing.dto.TransactionRow;
import com.hakjin.autocore.processing.fx.FxRateLookup;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FxCalculator {

    private final FxRateLookup fxRateLookup;

    public FxCalculator(FxRateLookup fxRateLookup) {
        this.fxRateLookup = fxRateLookup;
    }

    public List<KrwConvertedRow> convertAllToKrw(List<TransactionRow> rows) {
        if (rows == null || rows.isEmpty()) return List.of();

        List<KrwConvertedRow> out = new ArrayList<>(rows.size());

        for (TransactionRow row : rows) {
            if (row == null) continue;

            String currency = row.getCurrency();
            LocalDate postingDate = row.getPostingDate();
            BigDecimal amount = row.getAmount();

            if (currency == null || currency.isBlank() || postingDate == null || amount == null) {
                throw new IllegalStateException("Invalid row (currency/postingDate/amount) requestId=" + safe(row.getRequestId()));
            }

            BigDecimal rateToKrw = fxRateLookup
                    .findKrwPerUnit(currency, postingDate)
                    .orElseThrow(() -> new IllegalStateException(
                            "FX rate not found: currency=" + currency + ", date=" + postingDate + ", requestId=" + safe(row.getRequestId())
                    ));

            // KRW 금액 계산 (정책: 소수점 0자리 반올림)
            BigDecimal amountKrw = amount.multiply(rateToKrw)
                    .setScale(0, RoundingMode.HALF_UP);

            KrwConvertedRow converted = new KrwConvertedRow(
                    row.getRequestId(),
                    row.getPostingDate(),
                    row.getVendorCode(),
                    row.getDescription(),
                    row.getCurrency(),
                    row.getAmount(),
                    rateToKrw,
                    amountKrw
            );
            out.add(converted);
        }
        return out;
    }
    private String safe(String s) {
        return s == null ? "" : s;
    }
}