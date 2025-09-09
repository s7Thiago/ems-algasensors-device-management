package com.thiagosilva.algasensors.device.management.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorDetailOutput {
    private SensorOutput sensor;
    private SensorMonitoringOutput monitoring;
}
