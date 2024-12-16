package com.api.MyApi.utils;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetricsServiceCounter {

    private final Counter customMetricCounter;

    public CustomMetricsServiceCounter(MeterRegistry meterRegistry){

        customMetricCounter = Counter.builder("custom_metric").description("MY First Counter")
                .tags("enviroment","development").register(meterRegistry);

    }

    public void incrementCustomMetric(){

        customMetricCounter.increment();

    }





}
