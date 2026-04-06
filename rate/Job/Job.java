package com.hakjin.autocore.rate.Job;

import com.hakjin.autocore.rate.client.EcosFxClient;
import com.hakjin.autocore.rate.dto.EcosFxResponse;
import com.hakjin.autocore.rate.model.DailyFxRates;
import com.hakjin.autocore.rate.parser.EcosFxParser;
import com.hakjin.autocore.rate.store.FxRateStore;
import org.springframework.stereotype.Component;

@Component
public class Job {
    private final EcosFxClient ecosFxClient;
    private final EcosFxParser ecosFxParser;
    private final FxRateStore fxRateStore;

    public Job(EcosFxClient ecosFxClient, EcosFxParser ecosFxParser, FxRateStore fxRateStore) {
        this.ecosFxClient = ecosFxClient;
        this.ecosFxParser = ecosFxParser;
        this.fxRateStore = fxRateStore;
    }

    public void run() {
        System.out.println("=====ECOS API 호출 및 JSON FILE 저장 시작=====");
        EcosFxResponse response = ecosFxClient.client();
        DailyFxRates daily = ecosFxParser.parse(response);
        fxRateStore.save(daily);

        System.out.println("saved: " + daily.getPostingDate() + " (rates=" + daily.getRates().size() + ")");
    }
}
