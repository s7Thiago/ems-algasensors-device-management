package com.thiagosilva.algasensors.device.management.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensorInput {
    private String name;
    private String ip;
    private String location;
    private String protocol;
    private String model;

}
