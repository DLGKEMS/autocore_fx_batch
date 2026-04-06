package com.hakjin.autocore.rate.client;

import com.hakjin.autocore.rate.dto.EcosFxResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class EcosFxClient {
    @Value("${ecos.api.key}")
    private String api_key;

    private static final String BASE_URL = "https://ecos.bok.or.kr/api/StatisticSearch";
    private static final String STAT_CODE = "731Y001";
    private static final String CYCLE = "D";
    private static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final RestTemplate restTemplate = new RestTemplate();


    public EcosFxResponse client() {
        LocalDate day = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            String date = day.format(YMD);

            String url = String.format(
                    "%s/%s/json/kr/1/9999/%s/%s/%s/%s",
                    BASE_URL, api_key, STAT_CODE, CYCLE, date, date
            );

            EcosFxResponse response = restTemplate.getForObject(url, EcosFxResponse.class);

            if (response == null || response.getRows() == null || response.getRows().isEmpty()) {
                day = day.minusDays(1);
                continue;
            }
            System.out.println("rows=" + response.getRows().size() + " date=" + date);
            return response;

        }

        throw new RuntimeException("최근 7일 내 환율 데이터를 찾지 못했습니다: ");
    }
}
