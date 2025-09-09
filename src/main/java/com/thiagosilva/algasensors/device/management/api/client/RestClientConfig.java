package com.thiagosilva.algasensors.device.management.api.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {
    /**
     * Gera um RestClient de forma dinâmica aproveitando a interface
     * SensorMonitoringClient que foi modificado combinndo annotations 
     * do Spring para definir os endpoints que serão consumidos.
     * @param factory
     * @return
    */
    @Bean
    public SensorMonitoringClient sensorMonitoringClient(RestClientFactory factory) {
        RestClient client = factory.temperatureMonitoringClient();
        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builderFor(adapter).build();

        return proxyFactory.createClient(SensorMonitoringClient.class);

    }

}
