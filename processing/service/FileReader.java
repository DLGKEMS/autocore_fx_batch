package com.hakjin.autocore.processing.service;

import com.hakjin.autocore.processing.dto.TransactionRow;
import com.hakjin.autocore.dictionary.TransactionRowColumn;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileReader {

    public List<TransactionRow> reader(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            Map<String, Integer> headRowMap = makeMap(reader.readLine());
            return parsingTransactionRows(headRowMap, reader);
        }
    }
    /*public List<TransactionRow> reader(Path file){
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            Map<String, Integer> headRowMap = makeMap(reader.readLine());
            if(headRowMap == null || headRowMap.isEmpty()) {
                throw new IOException("헤더가 null이거나 비어있음"); // headRowMap이 null이거나 빈 경우
            }
            return parsingTransactionRows(headRowMap, reader);
        }
        catch (IOException e) {
            System.out.println("파일을 읽지 못함: " + file.toAbsolutePath());
            e.printStackTrace();
            return List.of();
        }
    }
*/
    public List<TransactionRow> parsingTransactionRows(Map<String, Integer> headers, BufferedReader reader) throws IOException {
        String line;
        List<TransactionRow> rows = new ArrayList<>();
        while((line = reader.readLine()) != null){
            String[] cells = line.split(",", -1);
            String requestIdRow = getCell(cells, headers.get("requestid"));
            String postingDateRaw = getCell(cells, headers.get("postingdate"));
            String vendorCodeRaw  = getCell(cells, headers.get("vendorcode"));
            String descriptionRaw = getCell(cells, headers.get("description"));
            String currencyRaw    = getCell(cells, headers.get("currency"));
            String amountRaw      = getCell(cells, headers.get("amount"));

            String requestId = TransactionRowColumn.REQUEST_ID.parse((requestIdRow));
            LocalDate postingDate = TransactionRowColumn.POSTING_DATE.parse((postingDateRaw));
            String vendorCode = TransactionRowColumn.VENDOR_CODE.parse(vendorCodeRaw); // 거래처 식별 코드
            String description = TransactionRowColumn.DESCRIPTION.parse(descriptionRaw); // 거래 내용에 대한 설명
            String currency = TransactionRowColumn.CURRENCY.parse(currencyRaw); // 통화 코드
            BigDecimal amount = TransactionRowColumn.AMOUNT.parse(amountRaw);// 거래 금액

            rows.add(new TransactionRow(requestId, postingDate, vendorCode, description, currency, amount));
        }
        return rows;
    }

    public Map<String, Integer> makeMap(String headLine){
        Map<String,Integer> headRowMap = new HashMap<>();

        if (headLine == null) {
            throw new IllegalStateException("헤더가 없음");
        } // empty파일 검사

        String[] headers = headLine.split(",", -1);

        for(int i = 0; i < headers.length; i++) {
            if(i == 0){
                headers[i] = headers[i].replace("\uFEFF", "");
            } // bom 제거

            headers[i] = headers[i].trim().toLowerCase().replace("_","");

            if(headers[i].isEmpty()) {
                throw new IllegalStateException("헤더에 공백이 있음");
            } // 공백 검사

            if(headRowMap.containsKey(headers[i])) {
                throw new IllegalStateException("헤더에 중복된 컬럼이 있음");
            } // 중복 검사

            headRowMap.put(headers[i], i);
        }

        List<String> missingHeaders = validateRequiredHeaders(headRowMap);
        if(!missingHeaders.isEmpty()) throw new IllegalStateException("필수 헤더 누락: " + missingHeaders); // 나중에 오류 메시지 작성 단계에서 다시 사용
        return headRowMap;
    } // header 표준화


    public List<String> validateRequiredHeaders(Map<String, Integer> headers){
        List<String> missingHeaders = new ArrayList<>();

        //  사전의 key 목록으로 필수 헤더 검사
        for (TransactionRowColumn col : TransactionRowColumn.values()) {
            String requiredKey = col.key();
            if (!headers.containsKey(requiredKey)) {
                missingHeaders.add(requiredKey);
            }
        }

        return missingHeaders;
    } // 필수 검증 메서드

    public String getCell(String[] cells, Integer idx){
       if(idx < 0 || idx >= cells.length) {
           throw new IllegalStateException("데이터 행의 컬럼 수가 부족");
       } // 칸 부족
       return cells[idx];
    } //cell 검증
}

