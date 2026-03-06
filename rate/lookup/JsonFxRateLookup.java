package com.hakjin.autocore.rate.lookup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakjin.autocore.processing.fx.FxRateLookup;
import com.hakjin.autocore.rate.model.DailyFxRates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JsonFxRateLookup implements FxRateLookup {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    private final ObjectMapper objectMapper;
    private final Path baseDir;

    private final ConcurrentHashMap<LocalDate, Optional<DailyFxRates>> cache = new ConcurrentHashMap<>();

    private final int lookbackDays;

    public JsonFxRateLookup(ObjectMapper objectMapper,
                            @Value("${fxrates.dir}") String baseDir,
                            @Value("${fxrates.lookback_days}") int lookbackDays) {
        this.objectMapper = objectMapper;
        this.baseDir = Paths.get(baseDir);
        this.lookbackDays = Math.max(0, lookbackDays);
    }

    @Override
    public Optional<BigDecimal> findKrwPerUnit(String currency, LocalDate postingDate) {
        if (currency == null || postingDate == null) return Optional.empty();

        String key = currency.trim().toUpperCase();
        if (key.isEmpty()) return Optional.empty();

        LocalDate d = postingDate;

        for (int i = 0; i <= lookbackDays; i++) {
            Optional<DailyFxRates> dailyOpt = cache.computeIfAbsent(d, this::loadOneDayOptional);

            if (dailyOpt.isPresent() && dailyOpt.get().getRates() != null) {
                BigDecimal rate = dailyOpt.get().getRates().get(key);
                if (rate != null) {
                    return Optional.of(rate);
                }
            }

            d = d.minusDays(1);
        }

        return Optional.empty();
    }

    private Optional<DailyFxRates> loadOneDayOptional(LocalDate date) {
        try {
            Path file = baseDir.resolve(ISO.format(date) + ".json");
            if (!Files.exists(file)) return Optional.empty();
            DailyFxRates daily = objectMapper.readValue(file.toFile(), DailyFxRates.class);
            return Optional.ofNullable(daily);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load fx rates json for date=" + date, e);
        }
    }
}
