package com.dimosr.config;

import com.dimosr.dependency.RemoteService;
import com.dimosr.dependency.servicecall.RemoteServiceCall;
import com.dimosr.monitoring.servicecall.GraphiteMetricsCollector;
import com.dimosr.service.ServiceCallBuilder;
import com.dimosr.service.core.MetricsCollector;
import com.dimosr.service.core.ServiceCall;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class MainConfiguration {

    private static final int CACHE_ENTRIES = 10_000;
    private static final int TTL_MILLIS = 5000;

    private static final int MAX_REQUESTS_PER_SECOND = 50;
    private static final int MAX_RETRIES = 3;
    private static final Duration TIMEOUT_THRESHOLD = Duration.ofMillis(1000);

    private static final int CIRCUIT_BREAKER_REQUESTS_WINDOW = 10;
    private static final int MAX_FAILING_REQUESTS_TO_OPEN = 5;
    private static final int CONSECUTIVE_SUCCESSFUL_REQUESTS_TO_CLOSE = 3;
    private static final int OPEN_STATE_DURATION_MILLIS = 10_000;


    private static final ExecutorService executor = new ThreadPoolExecutor(
            100,
            100,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()
    );

    @Bean
    public RemoteService remoteService() {
        return new RemoteService();
    }

    @Bean
    public MetricsCollector graphiteMetricsCollector() {
        return new GraphiteMetricsCollector("localhost", 2003);
    }

    @Bean
    ServiceCall<Integer, Integer> remoteServiceCall(
            final RemoteService remoteService,
            final MetricsCollector metricsCollector) {

        ServiceCall<Integer, Integer> remoteServiceCall = new RemoteServiceCall(remoteService);
        final String serviceCallID = "remote-1";
        return new ServiceCallBuilder<>(remoteServiceCall, serviceCallID)
                .withCache(CACHE_ENTRIES, TTL_MILLIS)
                .withMonitoring(metricsCollector)
                .withThrottling(MAX_REQUESTS_PER_SECOND)
                .withRetrying(true, MAX_RETRIES)
                .withCircuitBreaker(CIRCUIT_BREAKER_REQUESTS_WINDOW, MAX_FAILING_REQUESTS_TO_OPEN, CONSECUTIVE_SUCCESSFUL_REQUESTS_TO_CLOSE, OPEN_STATE_DURATION_MILLIS)
                .withTimeouts(TIMEOUT_THRESHOLD, TimeUnit.MILLISECONDS, executor)
                .build();
    }

}
