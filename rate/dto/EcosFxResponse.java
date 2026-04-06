package com.hakjin.autocore.rate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EcosFxResponse {

    @JsonProperty("StatisticSearch")
    private StatisticSearch statisticSearch;

    public List<EcosFxRowDto> getRows() {
        return statisticSearch == null ? null : statisticSearch.rows;
    }

    public static class StatisticSearch {
        @JsonProperty("row")
        private List<EcosFxRowDto> rows;
    }
}
