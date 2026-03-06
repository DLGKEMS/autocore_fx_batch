package com.hakjin.autocore.processing.service;

import com.hakjin.autocore.processing.dto.KrwConvertedRow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class KrwResultWriter {

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String HEADER =
            "REQUEST_ID,POSTING_DATE,VENDOR_CODE,DESCRIPTION,CURRENCY,AMOUNT,RATE_TO_KRW,AMOUNT_KRW";

    private final Path outDir;

    private final ThreadLocal<DecimalFormat> df;

    public KrwResultWriter(
            @Value("${app.dir_out:out}") String dirOut,
            @Value("${app.csv.number-format}") String numberFormatPattern
    ) {
        this.outDir = Paths.get(dirOut);

        this.df = ThreadLocal.withInitial(() -> new DecimalFormat(numberFormatPattern));
    }

    public Path writeKrwCsv(Path inputFile, List<KrwConvertedRow> convertedRows) {
        try {
            Files.createDirectories(outDir);

            String baseName = stripExt(inputFile.getFileName().toString());
            String outName = baseName + "_KRW_" + TS.format(LocalDateTime.now()) + ".csv";
            Path outFile = outDir.resolve(outName);

            try (BufferedWriter w = Files.newBufferedWriter(
                    outFile,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE_NEW
            )) {
                w.write(HEADER);
                w.newLine();

                for (KrwConvertedRow r : convertedRows) {
                    w.write(csv(r.getRequestId())); w.write(",");
                    w.write(csv(String.valueOf(r.getPostingDate()))); w.write(",");
                    w.write(csv(r.getVendorCode())); w.write(",");
                    w.write(csv(r.getDescription())); w.write(",");
                    w.write(csv(r.getCurrency())); w.write(",");
                    w.write(csv(fmt(r.getAmount()))); w.write(",");
                    w.write(csv(fmt(r.getRateToKrw()))); w.write(",");
                    w.write(csv(fmt(r.getAmountKrw())));
                    w.newLine();
                }
            }

            return outFile;
        } catch (Exception e) {
            throw new RuntimeException("Failed to write KRW csv for input=" + inputFile, e);
        }
    }

    private String stripExt(String filename) {
        int dot = filename.lastIndexOf('.');
        return (dot < 0) ? filename : filename.substring(0, dot);
    }

    private String fmt(BigDecimal v) {
        if (v == null) return "";
        return df.get().format(v);
    }

    // 나중에 한번 더 확인
    private String csv(String s) {
        if (s == null) return "";
        boolean needQuote = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String x = s.replace("\"", "\"\"");
        return needQuote ? "\"" + x + "\"" : x;
    }
}