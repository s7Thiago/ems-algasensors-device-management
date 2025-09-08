package com.thiagosilva.algasensors.device.management.api.config.jpa;

import io.hypersistence.tsid.TSID;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @Converter do jakarta.persistense carrega esse
 * conversor no carregamento do JPA semelhante ao
 * que foi feito para carregar o modulo costomizado
 * do jackson na pasta api/config/jackson
 * 
*/
@Converter(autoApply = true)
public class TSIDToLongJPAAttributeConnverter implements AttributeConverter<TSID, Long> {

    @Override
    public Long convertToDatabaseColumn(TSID attribute) {
        return attribute.toLong();
    }

    @Override
    public TSID convertToEntityAttribute(Long dbData) {
        return TSID.from(dbData);
    }
}
