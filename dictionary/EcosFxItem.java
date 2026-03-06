package com.hakjin.autocore.dictionary;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum EcosFxItem {

    // ===== 기준 통화 =====
    KRW("KRW", null, BigDecimal.ONE),                 // 대한민국 원 (기준통화)


    USD("USD", "0000001", BigDecimal.ONE),            // 미국 달러
    JPY("JPY", "0000002", new BigDecimal("100")),     // 일본 엔 (⚠ 100엔 기준)
    EUR("EUR", "0000003", BigDecimal.ONE),            // 유럽연합 유로
    GBP("GBP", "0000012", BigDecimal.ONE),            // 영국 파운드
    CAD("CAD", "0000013", BigDecimal.ONE),            // 캐나다 달러
    CHF("CHF", "0000014", BigDecimal.ONE),            // 스위스 프랑
    HKD("HKD", "0000015", BigDecimal.ONE),            // 홍콩 달러
    SEK("SEK", "0000016", BigDecimal.ONE),            // 스웨덴 크로나
    AUD("AUD", "0000017", BigDecimal.ONE),            // 호주 달러
    DKK("DKK", "0000018", BigDecimal.ONE),            // 덴마크 크로나
    NOK("NOK", "0000019", BigDecimal.ONE),            // 노르웨이 크로네
    SAR("SAR", "0000020", BigDecimal.ONE),            // 사우디아라비아 리얄
    KWD("KWD", "0000021", BigDecimal.ONE),            // 쿠웨이트 디나르
    BHD("BHD", "0000022", BigDecimal.ONE),            // 바레인 디나르
    AED("AED", "0000023", BigDecimal.ONE),            // 아랍에미리트 디르함
    SGD("SGD", "0000024", BigDecimal.ONE),            // 싱가포르 달러
    MYR("MYR", "0000025", BigDecimal.ONE),            // 말레이시아 링깃
    NZD("NZD", "0000026", BigDecimal.ONE),            // 뉴질랜드 달러
    THB("THB", "0000028", BigDecimal.ONE),            // 태국 바트
    IDR("IDR", "0000029", new BigDecimal("100")),            // 인도네시아 루피아
    TWD("TWD", "0000031", BigDecimal.ONE),            // 대만 달러
    MNT("MNT", "0000032", BigDecimal.ONE),            // 몽골 투그릭
    KZT("KZT", "0000033", BigDecimal.ONE),            // 카자흐스탄 텡게
    PHP("PHP", "0000034", BigDecimal.ONE),            // 필리핀 페소
    VND("VND", "0000035", new BigDecimal("100")),            // 베트남 동
    BND("BND", "0000036", BigDecimal.ONE), //브루니아 달러
    INR("INR", "0000037", BigDecimal.ONE),            // 인도 루피
    PKR("PKR", "0000038", BigDecimal.ONE),            // 파키스탄 루피
    BDT("BDT", "0000039", BigDecimal.ONE),            // 방글라데시 타카
    MXN("MXN", "0000040", BigDecimal.ONE),            // 멕시코 페소
    BRL("BRL", "0000041", BigDecimal.ONE),            // 브라질 헤알
    ARS("ARS", "0000042", BigDecimal.ONE),     // 아르헨티나 페소
    RUB("RUB", "0000043", BigDecimal.ONE),            // 러시아 루블
    HUF("HUF", "0000044", BigDecimal.ONE),            // 헝가리 포린트
    PLN("PLN", "0000045", BigDecimal.ONE),            // 폴란드 즐로티
    CZK("CZK", "0000046", BigDecimal.ONE),            // 체코 코루나
    QAR("QAR", "0000047", BigDecimal.ONE),            // 카타르 리얄
    ILS("ILS", "0000048", BigDecimal.ONE),            // 이스라엘 셰켈
    JOD("JOD", "0000049", BigDecimal.ONE),            // 요르단 디나르
    TRY("TRY", "0000050", BigDecimal.ONE),      //튀르키예 리라
    ZAR("ZAR", "0000051", BigDecimal.ONE),            // 남아프리카공화국 랜드
    EGP("EGP", "0000052", BigDecimal.ONE),            // 이집트 파운드
    CNY("CNY", "0000053", BigDecimal.ONE);            // 중국 위안


    private final String currency; // 통화 코드
    private final String ecosItemCode; //ecos 코드
    private final BigDecimal unit; // 기준 단위 (일본 엔화 때문에 사용)

    EcosFxItem(String currency, String ecosItemCode, BigDecimal unit) {
        this.currency = currency;
        this.ecosItemCode = ecosItemCode;
        this.unit = unit;
    }

    public String currency() {
        return currency;
    }

    public String ecosItemCode() {
        return ecosItemCode;
    }

    public BigDecimal unit() {
        return unit;
    }

    private static final Map<String, EcosFxItem> BY_CURRENCY =
            Arrays.stream(values()).collect(Collectors.toMap(EcosFxItem::currency, e -> e));

    public static EcosFxItem fromCurrency(String currency) {
        if (currency == null) return null;
        return BY_CURRENCY.get(currency.trim().toUpperCase());
    }
    // 역조회 ex) 000001 -> USD
    private static final Map<String, EcosFxItem> BY_ECOS_ITEM_CODE =
            Arrays.stream(values())
                    .filter(e -> e.ecosItemCode != null)
                    .collect(Collectors.toMap(EcosFxItem::ecosItemCode, e -> e));

    public static EcosFxItem fromEcosItemCode(String ecosItemCode) {
        if (ecosItemCode == null) return null;
        return BY_ECOS_ITEM_CODE.get(ecosItemCode.trim());
    }
}