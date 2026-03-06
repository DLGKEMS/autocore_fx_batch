package com.hakjin.autocore.dictionary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public enum TransactionRowColumn {

    REQUEST_ID(
            "requestid",
            s -> s == null ? null : s.trim()
    ),

    POSTING_DATE(
            "postingdate",
            s -> {
                if (s == null) return null;
                String v = s.trim();
                if (v.isEmpty()) return null;

                // 2026-02-05
                if (v.contains("-")) {
                    return LocalDate.parse(v);
                }
                // 20260205
                return LocalDate.parse(v, DateTimeFormatter.BASIC_ISO_DATE);
            }
    ),

    VENDOR_CODE(
            "vendorcode",
            s -> s == null ? null : s.trim()
    ),

    DESCRIPTION(
            "description",
            s -> s == null ? null : s.trim()
    ),

    CURRENCY(
            "currency",
            s -> s == null ? null : s.trim().toUpperCase()
    ),

    AMOUNT(
            "amount",
            s -> {
                if (s == null) return null;
                String v = s.trim();
                if (v.isEmpty()) return null;

                v = v.replace(",", ""); // 천단위 콤마 제거
                return new BigDecimal(v);
            }
    );

    private final String key;
    private final Function<String, ?> parser;

    TransactionRowColumn(String key, Function<String, ?> parser) {
        this.key = key;
        this.parser = parser;
    }


    /** CSV 헤더 정규화 키 */
    public String key() {
        return key;
    }

    /** CSV 문자열 → DTO 필드 타입 변환 */
    @SuppressWarnings("unchecked")
    public <T> T parse(String raw) {
        return (T) parser.apply(raw);
    }
}
