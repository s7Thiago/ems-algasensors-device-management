package com.thiagosilva.algasensors.device.management.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thiagosilva.algasensors.device.management.domain.model.Sensor;
import com.thiagosilva.algasensors.device.management.domain.model.SensorId;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, SensorId> {
    
}
