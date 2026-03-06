package com.hakjin.autocore.rate.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakjin.autocore.rate.model.DailyFxRates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class JsonFxRateStore implements FxRateStore {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE; // yyyy-MM-dd

    private final ObjectMapper objectMapper;
    private final Path baseDir;

    public JsonFxRateStore(
            ObjectMapper objectMapper,
            @Value("${fxrates.dir:rates}") String baseDir
    ) {
        this.objectMapper = objectMapper;
        this.baseDir = Paths.get(baseDir);
    }

    @Override
    public void save(DailyFxRates daily) {
        if (daily == null) throw new IllegalArgumentException("daily is null");

        LocalDate date = daily.getPostingDate();
        if (date == null) throw new IllegalArgumentException("postingDate is null");

        try {
            Files.createDirectories(baseDir);

            Path target = baseDir.resolve(ISO.format(date) + ".json");
            Path tmp = baseDir.resolve(ISO.format(date) + ".json.tmp");
            Path bak = baseDir.resolve(ISO.format(date) + ".json.bak");

            // 1) tmp에 먼저 쓰기
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(tmp.toFile(), daily);

            // 2) 기존 파일 있으면 백업(.bak 교체)
            if (Files.exists(target)) {
                Files.move(target, bak, StandardCopyOption.REPLACE_EXISTING);
            }

            // 3) tmp -> target
            try {
                Files.move(tmp, target,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp, target, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to save fx rates json", e);
        }
    }
}
