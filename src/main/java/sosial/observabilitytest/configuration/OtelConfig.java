package sosial.observabilitytest.configuration;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;

import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;

import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;

import io.opentelemetry.sdk.resources.Resource;

import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class OtelConfig {

    @Bean
    public OpenTelemetry openTelemetry(
            @Value("${otel.exporter.otlp.endpoint:http://localhost:4317}") String otlpEndpoint,
            @Value("${otel.service.name:payments-service}") String serviceName,
            @Value("${otel.service.version:0.0.1}") String serviceVersion,
            @Value("${otel.deployment.environment:local}") String environment,
            @Value("${otel.metrics.export.interval:10s}") Duration metricsInterval
    ) {
        // =========================
        // Common Resource (traces + metrics)
        // =========================
        Resource resource = Resource.getDefault().merge(
                Resource.create(Attributes.of(
                        AttributeKey.stringKey("service.name"), serviceName,
                        AttributeKey.stringKey("service.version"), serviceVersion,
                        AttributeKey.stringKey("deployment.environment"), environment
                ))
        );

        // =========================
        // TRACES: OTLP gRPC -> Collector
        // =========================
        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint(otlpEndpoint) // host app => "http://localhost:4317"
                .setTimeout(Duration.ofSeconds(5))
                .build();

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                .build();

        // =========================
        // METRICS: OTLP gRPC -> Collector
        // =========================
        OtlpGrpcMetricExporter metricExporter = OtlpGrpcMetricExporter.builder()
                .setEndpoint(otlpEndpoint) // same OTLP endpoint
                .setTimeout(Duration.ofSeconds(5))
                .build();

        PeriodicMetricReader metricReader = PeriodicMetricReader.builder(metricExporter)
                .setInterval(metricsInterval) // default 10s
                .build();

        SdkMeterProvider meterProvider = SdkMeterProvider.builder()
                .setResource(resource)
                .registerMetricReader(metricReader)
                .build();

        // =========================
        // SDK: install both tracer + meter providers
        // =========================
        OpenTelemetrySdk sdk = OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .setMeterProvider(meterProvider)
                .build();

        // If your library uses GlobalOpenTelemetry.get()
        GlobalOpenTelemetry.set(sdk);

        return sdk;
    }

    @Bean
    public DisposableBean otelShutdown(OpenTelemetry openTelemetry) {
        return () -> {
            if (openTelemetry instanceof OpenTelemetrySdk sdk) {
                // stop metrics first (flush/export), then traces
                sdk.getSdkMeterProvider().shutdown().join(5, TimeUnit.SECONDS);
                sdk.getSdkTracerProvider().shutdown().join(5, TimeUnit.SECONDS);
            }
        };
    }
}
