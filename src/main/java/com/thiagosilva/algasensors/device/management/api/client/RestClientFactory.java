package com.thiagosilva.algasensors.device.management.api.client;

import java.time.Duration;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestClientFactory {

    private final RestClient.Builder builder;

    // instancia o client chamando o bean de builder que o Spring deixa pré carregado
    public RestClient temperatureMonitoringClient() {
        return builder
                .baseUrl("http://localhost:8081")
                .requestFactory(generateClientRequestFactory())
                .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                    throw new SensorMonitoringClientBadGatewayException();
                }).build();
    }

    private ClientHttpRequestFactory generateClientRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(3));
        return factory;
    }
}
