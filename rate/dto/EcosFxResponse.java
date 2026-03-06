package com.hakjin.autocore.rate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EcosFxResponse {

    @JsonProperty("StatisticSearch")
    private StatisticSearch statisticSearch;

    public List<EcosFxRowDto> getRows() {
        return statisticSearch == null ? null : statisticSearch.rows;
    }

    public Integer getTotalCount() {
        return statisticSearch == null ? null : statisticSearch.listTotalCount;
    }

    public static class StatisticSearch {

        @JsonProperty("list_total_count")
        private Integer listTotalCount;

        @JsonProperty("row")
        private List<EcosFxRowDto> rows;
    }
}
