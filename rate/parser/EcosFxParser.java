package com.hakjin.autocore.rate.parser;

import com.hakjin.autocore.dictionary.EcosFxItem;
import com.hakjin.autocore.rate.dto.EcosFxResponse;
import com.hakjin.autocore.rate.dto.EcosFxRowDto;
import com.hakjin.autocore.rate.model.DailyFxRates;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class EcosFxParser {

    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.BASIC_ISO_DATE;

    public DailyFxRates parse(EcosFxResponse response) {
        List<EcosFxRowDto> rows = response == null ? null : response.getRows();
        if (rows == null || rows.isEmpty()) {
            throw new IllegalArgumentException("EcosFxResponse가 null이거나 비어있음");
        }

        LocalDate postingDate = LocalDate.parse(rows.get(0).getTime(), YYYYMMDD);

        Map<String, BigDecimal> rates = new LinkedHashMap<>();

        for (EcosFxRowDto row : rows) {
            if (row == null) continue;

            EcosFxItem item = EcosFxItem.fromEcosItemCode(row.getItemCode());
            // 사전에 없는 통화면 스킵
            if (item == null) {
                continue;
            }

            String rateStr = row.getRate();
            if (rateStr == null || rateStr.isBlank()) continue;

            BigDecimal raw = new BigDecimal(rateStr);

            BigDecimal normalized = raw.divide(item.unit(), 10, RoundingMode.HALF_UP);

            rates.put(item.currency(), normalized);
        }

        return new DailyFxRates(rates, postingDate);
    }
}
