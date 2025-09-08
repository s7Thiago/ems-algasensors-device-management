package com.thiagosilva.algasensors.device.management.api.client.impl;

import java.time.Duration;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.thiagosilva.algasensors.device.management.api.client.SensorMonitoringClient;
import com.thiagosilva.algasensors.device.management.api.client.SensorMonitoringClientBadGatewayException;

import io.hypersistence.tsid.TSID;

@Component
public class SensorMonitoringClientImpl implements SensorMonitoringClient {

    private final RestClient client;

    // instancia o client chamando o bean de builder que o Spring deixa pré
    // carregado
    public SensorMonitoringClientImpl(RestClient.Builder builder) {
        this.client = builder
                .baseUrl("http://localhost:8081")
                .requestFactory(generateClientRequestFactory())
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    throw new SensorMonitoringClientBadGatewayException();
                })
                .build();
    }

    private ClientHttpRequestFactory generateClientRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(3));
        return factory;
    }

    @Override
    public void enableMonitoring(TSID sensorId) {
        client.put()
                .uri("/api/sensors/{sensorId}/monitoring/enable", sensorId)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void disableMonitoring(TSID sensorId) {
        client.delete()
                .uri("/api/sensors/{sensorId}/monitoring/enable", sensorId)
                .retrieve()
                .toBodilessEntity();
    }

}
