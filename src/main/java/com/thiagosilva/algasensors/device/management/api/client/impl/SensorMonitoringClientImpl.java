package com.thiagosilva.algasensors.device.management.api.client.impl;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.thiagosilva.algasensors.device.management.api.client.RestClientFactory;
import com.thiagosilva.algasensors.device.management.api.client.SensorMonitoringClient;
import com.thiagosilva.algasensors.device.management.api.model.SensorMonitoringOutput;

import io.hypersistence.tsid.TSID;

// @Component Comentado pois não é mais usado devido a criação do RestClientConfig para gerar o client dinamicamente
public class SensorMonitoringClientImpl implements SensorMonitoringClient {

    private final RestClient client;

    public SensorMonitoringClientImpl(RestClientFactory factory) {
        this.client = factory.temperatureMonitoringClient();
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

    @Override
    public SensorMonitoringOutput getDetail(TSID sensorId) {
        return client.get()
                .uri("/api/sensors/{sensorId}/monitoring", sensorId)
                .retrieve()
                .body(SensorMonitoringOutput.class);
        
    }

}
