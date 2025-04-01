package com.real_property_system_api.real_property_system.configs;

import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class MetricsConfig 
{
    public MetricsConfig(MeterRegistry registry) {
        // Register some basic gauges
        registry.gauge("application.ready", 1);
    }
}
