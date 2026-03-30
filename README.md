# autocore_fx_batch
한국은행 ECOS Open API와 환율 JSON 캐시를 활용해 CSV 외화 데이터를 KRW로 변환하는 Spring Boot 배치 프로그램

## Batch 처리 흐름 및 구조
![FX_Batch_Processing_Flow](./FX_Batch_Processing_Flow.png)

## 설계포인트
1. 파일을 처리할때 선점 구조를 적용하여 처리 상태를 구분하고 파일의 위치에 따른 상태 표현이 되도록 설계. 이후 병렬 처리로 확장하더라도 동일한 흐름을 유지할 수 있도록 고려.
2. fallback로직을 적용하여 기준일 환율 데이터가 없는 경우에도 프로그램이 중단되지 않고 이전 날짜를 조회하여 처리하도록 설계.
3. 환율 데이터를 날짜별 JSON 파일로 관리해 외부 API 호출을 최소화하고, 조회한 데이터는 Map에 캐싱하여 반복 조회 시 파일 I/O 없이 기존 데이터를 재사용하도록 설계.
4. 파일 처리 영역과 환율 관리 영역을 processing과 rate 패키지로 분리하여 이후 확장이나 변경에 유연하게 대응하도록 설계

## 기술스택
Java, Spring Boot, JSON / Jackson
