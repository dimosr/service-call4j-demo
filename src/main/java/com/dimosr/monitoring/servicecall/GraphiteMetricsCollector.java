package com.dimosr.monitoring.servicecall;

import com.dimosr.monitoring.graphite.SimpleGraphiteClient;
import com.dimosr.service.core.MetricsCollector;

import java.time.Instant;

/**
 * Implementation of a MetricsCollector using a Graphite backend
 */
public class GraphiteMetricsCollector implements MetricsCollector {
    private final SimpleGraphiteClient graphiteClient;

    public GraphiteMetricsCollector(final String hostname, final int port) {
        graphiteClient = new SimpleGraphiteClient(hostname, port);
    }

    @Override
    public void putMetric(final String namespace, final double value, final Instant timestamp) {
        long epochInSeconds = timestamp.toEpochMilli()/1000;
        graphiteClient.sendMetric(namespace, value, epochInSeconds);
    }
}
