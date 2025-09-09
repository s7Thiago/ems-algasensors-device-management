package com.thiagosilva.algasensors.device.management.api.config.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import io.hypersistence.tsid.TSID;

public class StringToTSIDDeserializer extends JsonDeserializer<TSID> {

    /**
     * Nesse caso, possibilitou que a chamada do REST client que recebe um TSID na
     * resposta consiga desserializar o valor corretamente de String para TSID.
     * 
     */
    @Override
    public TSID deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return TSID.from(p.getText());
    }

}
