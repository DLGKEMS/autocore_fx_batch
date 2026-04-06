package com.hakjin.autocore.rate.scheduler;

import com.hakjin.autocore.rate.Job.Job;
import com.hakjin.autocore.rate.client.EcosFxClient;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class DailyFxRateScheduler {
    private final Job job;
    public  DailyFxRateScheduler(Job job, EcosFxClient ecos) {
        this.job = job;
    }

    @Scheduled(cron = "${app.fx.cron}", zone = "${app.zone}")
    public void scheduleDailyFxRate() {
        System.out.println("=========JSON 파일 생성 시작=========");
        job.run();
    }
}
