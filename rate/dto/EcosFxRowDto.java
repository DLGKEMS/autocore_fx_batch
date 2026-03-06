package com.hakjin.autocore.rate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EcosFxRowDto {

    @JsonProperty("ITEM_CODE1")
    private String itemCode;   // ECOS 통화 코드 (0000001)

    @JsonProperty("DATA_VALUE")
    private String rate;       // 환율 값

    @JsonProperty("TIME")
    private String time;       // 기준일 yyyyMMdd

    @JsonProperty("ITEM_NAME1")
    private String itemName1;

    public String getItemCode() {
        return itemCode;
    }

    public String getRate() {
        return rate;
    }

    public String getItemName1(){
        return itemName1;
    }

    public String getTime() {
        return time;
    }
}
