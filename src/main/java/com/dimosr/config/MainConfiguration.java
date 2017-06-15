package com.dimosr.config;

import com.dimosr.cache.GuavaCache;
import com.dimosr.dependency.RemoteService;
import com.dimosr.dependency.servicecall.RemoteServiceCall;
import com.dimosr.monitoring.servicecall.GraphiteMetricsCollector;
import com.dimosr.service.ServiceCallBuilder;
import com.dimosr.service.core.Cache;
import com.dimosr.service.core.MetricsCollector;
import com.dimosr.service.core.ServiceCall;
import com.google.common.cache.CacheBuilder;
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

    private static final int MAX_REQUESTS_PER_SECOND = 50;
    private static final int MAX_RETRIES = 3;
    private static final Duration TIMEOUT_THRESHOLD = Duration.ofMillis(500);

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
            final Cache<Integer, Integer> cache,
            final MetricsCollector metricsCollector) {

        ServiceCall<Integer, Integer> remoteServiceCall = new RemoteServiceCall(remoteService);
        return new ServiceCallBuilder<>(remoteServiceCall)
                .withCache(cache)
                .withMonitoring(metricsCollector)
                .withThrottling(MAX_REQUESTS_PER_SECOND)
                .withRetrying(true, MAX_RETRIES)
                .withTimeouts(TIMEOUT_THRESHOLD, TimeUnit.MILLISECONDS, executor)
                .build();
    }

    @Bean
    public Cache<Integer, Integer> cache() {
        return new GuavaCache<>(
             CacheBuilder.newBuilder()
            .maximumSize(CACHE_ENTRIES)
            .build()
        );
    }

}
