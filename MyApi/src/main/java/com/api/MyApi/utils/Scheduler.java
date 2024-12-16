package com.api.MyApi.utils;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
class Scheduler {
    private final TestService testService;

    public Scheduler(TestService testService) {
        this.testService = testService;
    }

    @Scheduled(fixedRate = 1000)
    public void schedule() {
        testService.testApi();
        testService.recordQueueLength();
    }
}
