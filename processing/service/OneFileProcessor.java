package com.hakjin.autocore.processing.service;

import com.hakjin.autocore.processing.dto.KrwConvertedRow;
import com.hakjin.autocore.processing.dto.TransactionRow;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class OneFileProcessor {

    private final FileReader fileReader;
    private final FxCalculator fxCalculator;
    private final KrwResultWriter krwResultWriter;
    private final FileMover fileMover;

    public OneFileProcessor(FileReader fileReader, FxCalculator fxCalculator, KrwResultWriter krwResultWriter, FileMover fileMover) {
        this.fileReader = fileReader;
        this.fxCalculator = fxCalculator;
        this.krwResultWriter = krwResultWriter;
        this.fileMover = fileMover;
    }

    /** 입력: IN에 있는 csv 파일 1개 */
    public void process(Path inFile) {
        Path processingFile = null;

        try {
            // 1) 선점 (IN -> PROCESSING)
            processingFile = fileMover.claimToProcessing(inFile);

            // 2) 처리
            List<TransactionRow> rows = fileReader.reader(processingFile);
            List<KrwConvertedRow> converted = fxCalculator.convertAllToKrw(rows);
            krwResultWriter.writeKrwCsv(processingFile, converted);

            // 3) 성공
            fileMover.moveToDone(processingFile);

        } catch (Exception processingFail) {
            // 4) 실패
            System.out.println("Error processing file: " + processingFile);
            processingFail.printStackTrace();
            if (processingFile != null) {
                try {
                    fileMover.moveToError(processingFile);
                } catch (Exception moveFail) {
                    moveFail.addSuppressed(processingFail);
                    System.err.println("CRITICAL: moveToError failed: " + processingFile);
                    moveFail.printStackTrace();
                }
            } else {
                // 파일 선점 실패
                System.err.println("SKIP (cannot claim): " + inFile + " / " + processingFail.getMessage());
                processingFail.printStackTrace();
            }
        }
    }
}