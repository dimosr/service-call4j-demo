package com.dimosr.config;

import com.dimosr.dependency.RemoteService;
import com.dimosr.monitoring.servicecall.GraphiteMetricsCollector;
import com.dimosr.service.core.MetricsCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfiguration {

    @Bean
    public RemoteService remoteService() {
        return new RemoteService();
    }

    @Bean
    public MetricsCollector graphiteMetricsCollector() {
        return new GraphiteMetricsCollector("localhost", 2003);
    }

}
