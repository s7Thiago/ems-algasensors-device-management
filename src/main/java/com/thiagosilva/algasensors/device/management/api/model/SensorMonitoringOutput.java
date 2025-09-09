package com.thiagosilva.algasensors.device.management.api.model;

import java.time.OffsetDateTime;

import io.hypersistence.tsid.TSID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorMonitoringOutput {
    private TSID id;
    private Double lastTemperature;
    private OffsetDateTime updatedAt;
    private Boolean enabled;
    
}
