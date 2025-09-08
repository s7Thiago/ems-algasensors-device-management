package com.thiagosilva.algasensors.device.management.api.config.web;

import org.springframework.core.convert.converter.Converter;

import io.hypersistence.tsid.TSID;

// Aplicando um conversor de String para TSID no Spring para possibilitar
// que parâmetros em métodos de endpoint possam ser tipados com a classe 
// TSID e será propagado pelo WebConfig.java
public class StringToTSIDWebConverter implements Converter<String, TSID>{

    @Override
    public TSID convert(String source) {
        return TSID.from(source);
    }
    
}
