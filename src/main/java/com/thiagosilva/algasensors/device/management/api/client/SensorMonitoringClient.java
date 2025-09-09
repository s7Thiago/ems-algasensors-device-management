package com.thiagosilva.algasensors.device.management.api.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

import com.thiagosilva.algasensors.device.management.api.model.SensorMonitoringOutput;

import io.hypersistence.tsid.TSID;

@HttpExchange("/api/sensors/{sensorId}/monitoring")
public interface SensorMonitoringClient {

    @PutExchange("/enable")
    void enableMonitoring(@PathVariable("sensorId") TSID sensorId);

    @DeleteExchange("/enable")
    void disableMonitoring(@PathVariable("sensorId") TSID sensorId);

    @GetExchange
    SensorMonitoringOutput getDetail(@PathVariable("sensorId") TSID sensorId);

}
