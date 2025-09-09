package com.thiagosilva.algasensors.device.management.api.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.thiagosilva.algasensors.device.management.api.client.SensorMonitoringClient;
import com.thiagosilva.algasensors.device.management.api.model.SensorDetailOutput;
import com.thiagosilva.algasensors.device.management.api.model.SensorInput;
import com.thiagosilva.algasensors.device.management.api.model.SensorOutput;
import com.thiagosilva.algasensors.device.management.common.IdGenerator;
import com.thiagosilva.algasensors.device.management.domain.model.Sensor;
import com.thiagosilva.algasensors.device.management.domain.model.SensorId;
import com.thiagosilva.algasensors.device.management.domain.repository.SensorRepository;

import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorRepository repository;
    private final SensorMonitoringClient sensorMonitoringClient;

    @GetMapping("{sensorId}/detail")
    public SensorDetailOutput getOneWithDetail(@PathVariable(name = "sensorId") TSID sensorId) {

        Sensor sensor = repository
                .findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Sensor %s não encontrado", sensorId.toString())));

        var details = sensorMonitoringClient.getDetail(sensorId);
        var sensorDto = toDTO(sensor);

        return SensorDetailOutput.builder()
                .sensor(sensorDto)
                .monitoring(details)
                .build();
    }

    // ! Para possibilitar a passagem desse param do tipo TSD, foi necessário criar
    // ! um conversor para o spring (não para o jackson ou para o JPA)
    @DeleteMapping("{sensorId}/enable")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void disableOne(@PathVariable(name = "sensorId") TSID sensorId) {

        Sensor sensor = repository
                .findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Sensor %s não encontrado", sensorId.toString())));

        sensor.setEnabled(false);
        repository.saveAndFlush(sensor);

        sensorMonitoringClient.disableMonitoring(sensorId);
    }

    // ! Para possibilitar a passagem desse param do tipo TSD, foi necessário criar
    // ! um conversor para o spring (não para o jackson ou para o JPA)
    @PutMapping("{sensorId}/enable")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void enableOne(@PathVariable(name = "sensorId") TSID sensorId) {

        Sensor sensor = repository
                .findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Sensor %s não encontrado", sensorId.toString())));

        sensor.setEnabled(true);
        repository.saveAndFlush(sensor);

        sensorMonitoringClient.enableMonitoring(sensorId);
    }

    // ! Para possibilitar a passagem desse param do tipo TSD, foi necessário criar
    // ! um conversor para o spring (não para o jackson ou para o JPA)
    @PutMapping("{sensorId}")
    @ResponseStatus(code = HttpStatus.OK)
    public SensorOutput updateOne(@PathVariable(name = "sensorId") TSID sensorId, @RequestBody SensorInput input) {

        Sensor sensor = repository
                .findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Sensor %s não encontrado", sensorId.toString())));

        boolean active = sensor.getEnabled() == null ? false : sensor.getEnabled();
        sensor = toEntity(sensorId, input);
        sensor.setEnabled(active);
        repository.saveAndFlush(sensor);

        return toDTO(sensor);
    }

    // ! Para possibilitar a passagem desse param do tipo TSD, foi necessário criar
    // ! um conversor para o spring (não para o jackson ou para o JPA)
    @DeleteMapping("{sensorId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOne(@PathVariable("sensorId") TSID sensorId) {

        Sensor sensor = repository
                .findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Sensor %s não encontrado", sensorId.toString())));

        Optional.ofNullable(sensor)
                .ifPresent(s -> repository.delete(s));

        sensorMonitoringClient.disableMonitoring(sensorId);
    }

    @GetMapping
    public Page<SensorOutput> search(@PageableDefault Pageable pageable) {
        Page<Sensor> sensors = repository.findAll(pageable);
        return sensors.map(this::toDTO);
    }

    // ! Para possibilitar a passagem desse param do tipo TSD, foi necessário criar
    // ! um conversor para o spring (não para o jackson ou para o JPA)
    @GetMapping("{sensorId}")
    public SensorOutput getOne(@PathVariable(name = "sensorId") TSID sensorId) {

        Sensor sensor = repository
                .findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Sensor %s não encontrado", sensorId.toString())));

        return toDTO(sensor);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public SensorOutput create(@RequestBody SensorInput input) {
        Sensor sensor = Sensor.builder()
                .id(new SensorId(IdGenerator.generateTSID()))
                .name(input.getName())
                .ip(input.getIp())
                .location(input.getLocation())
                .protocol(input.getProtocol())
                .model(input.getModel())
                .enabled(false)
                .build();

        sensor = repository.saveAndFlush(sensor);

        return toDTO(sensor);
    }

    private SensorOutput toDTO(Sensor sensor) {
        return SensorOutput.builder()
                .id(sensor.getId().getValue())
                .name(sensor.getName())
                .ip(sensor.getIp())
                .location(sensor.getLocation())
                .protocol(sensor.getProtocol())
                .model(sensor.getModel())
                .enabled(false)
                .build();
    }

    private Sensor toEntity(TSID id, SensorInput sensor) {
        return Sensor.builder()
                .id(new SensorId(id))
                .name(sensor.getName())
                .ip(sensor.getIp())
                .location(sensor.getLocation())
                .protocol(sensor.getProtocol())
                .model(sensor.getModel())
                .build();
    }
}
