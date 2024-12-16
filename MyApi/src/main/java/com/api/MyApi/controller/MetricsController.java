package com.api.MyApi.controller;

import com.api.MyApi.utils.TestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Controller {
    private final TestService testService;

    public Controller(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/setMeanResponseTimeSeconds")
    public void setResponseTimeSecondsMean(@RequestBody double seconds) {
        testService.setMeanResponseTimeSeconds(seconds);
    }

    @PostMapping("/setMeanQueueLength")
    public void setMeanQueueLength(@RequestBody double mean) {
        testService.setMeanQueueLength(mean);
    }
}
