package com.api.MyApi.utils;


import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TestService {
    private static final String TEST_SERVICE_RESPONSE_TIME_SECONDS = "test.service.response.time.seconds";
    private static final String TEST_SERVICE_QUEUE_LENGTH_HISTOGRAM = "test.service.queue.length.histogram";
    private static final String TEST_SERVICE_QUEUE_LENGTH_SUMMARY = "test.service.queue.length.summary";
    private final Timer testServiceResponseTimeSeconds;
    private final DistributionSummary testServiceQueueLengthHistogram;
    private final DistributionSummary testServiceQueueLengthSummary;
    private final RandomDataGenerator randomDataGenerator;
    private NormalDistribution serviceResponseTimeSecondsDistribution;
    private PoissonDistribution meanQueueLengthDistribution;

    public TestService(
            @Value("${spring.application.test.service.mean.response.time.seconds}")
            double meanResponseTimeSeconds,
            @Value("${spring.application.test.service.stddev.response.time.seconds}")
            double stddevResponseTimeSeconds,
            @Value("${spring.application.test.service.mean.queue.length}") double meanQueueLength,
            MeterRegistry meterRegistry) {

        Logger log = LoggerFactory.getLogger(TestService.class);


        // Inicializando o RandomDataGenerator
        this.randomDataGenerator = new RandomDataGenerator();

        // Distribuição Normal
        this.serviceResponseTimeSecondsDistribution =
                new NormalDistribution(meanResponseTimeSeconds, stddevResponseTimeSeconds);

        // Distribuição Poisson
        this.meanQueueLengthDistribution = new PoissonDistribution(meanQueueLength);

        // this will be published as a Prometheus histogram
        this.testServiceQueueLengthHistogram =
                DistributionSummary.builder(TEST_SERVICE_QUEUE_LENGTH_HISTOGRAM)
                        .maximumExpectedValue(20.0)
                        .publishPercentileHistogram()
                        .register(meterRegistry);

        // this will be published as a Prometheus summary
        this.testServiceQueueLengthSummary =
                DistributionSummary.builder(TEST_SERVICE_QUEUE_LENGTH_SUMMARY)
                        .maximumExpectedValue(20.0)
                        .publishPercentiles(0.25, 0.5, 0.75, 0.95)
                        .register(meterRegistry);

        // this will be published as a Prometheus histogram
        this.testServiceResponseTimeSeconds =
                Timer.builder(TEST_SERVICE_RESPONSE_TIME_SECONDS)
                        .serviceLevelObjectives(
                                Duration.ofMillis(100),
                                Duration.ofMillis(200),
                                Duration.ofMillis(300),
                                Duration.ofMillis(400),
                                Duration.ofMillis(500),
                                Duration.ofMillis(600),
                                Duration.ofMillis(700),
                                Duration.ofMillis(800),
                                Duration.ofMillis(900),
                                Duration.ofMillis(1000),
                                Duration.ofMillis(1100),
                                Duration.ofMillis(1200),
                                Duration.ofMillis(1300),
                                Duration.ofMillis(1400),
                                Duration.ofMillis(1500))
                        .maximumExpectedValue(Duration.ofMillis(1500))
                        .register(meterRegistry);
    }

    public void testApi() {
        Logger log = LoggerFactory.getLogger(TestService.class);

        // Gerar tempo de resposta baseado na distribuição normal
        double responseTimeSeconds = serviceResponseTimeSecondsDistribution.sample();
        long responseTimeMillis = (long) (responseTimeSeconds * 1_000);
        testServiceResponseTimeSeconds.record(Duration.ofMillis(responseTimeMillis));

        log.debug("The response time is {} ms", responseTimeMillis);
    }

    public void recordQueueLength() {

        Logger log = LoggerFactory.getLogger(TestService.class);

        // Gerar comprimento da fila baseado na distribuição Poisson
        int queueLength = meanQueueLengthDistribution.sample();
        testServiceQueueLengthHistogram.record(queueLength);
        testServiceQueueLengthSummary.record(queueLength);
        log.debug("The queue length {}", queueLength);
    }

    public void setMeanResponseTimeSeconds(double newMean) {
        serviceResponseTimeSecondsDistribution = new NormalDistribution(newMean, serviceResponseTimeSecondsDistribution.getStandardDeviation());
    }

    public void setMeanQueueLength(double mean) {
        meanQueueLengthDistribution = new PoissonDistribution(mean);
    }
}

