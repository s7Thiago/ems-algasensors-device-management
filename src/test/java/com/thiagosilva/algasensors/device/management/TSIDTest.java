package com.thiagosilva.algasensors.device.management;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.thiagosilva.algasensors.device.management.common.IdGenerator;

import io.hypersistence.tsid.TSID;
import io.hypersistence.tsid.TSID.Factory;

public class TSIDTest {

    @Test
    public void shouldGenerateTSID() {

        // não usar em produção. ao invés disso, usar o factory
        // O TSID precisa de informações como o n° do nó e de instâncias/máquinas
        // para gerar os identificadores corretamente minimizando a possibilidade de
        // colisão

        TSID tsid = TSID.fast();
        System.out.printf("TSID gerado (texto): %s\n", tsid);
        System.out.printf("TSID gerado (num): %d\n", tsid.toLong());
        System.out.printf("TSID gerado (instante): %s\n", tsid.getInstant());

        System.out.println("======================================");
        
        // instanciando um TSID via um factory padrão que lê propriedades do ambiente
        TSID tsid2 = TSID.Factory.getTsid();
        System.out.printf("TSID (factory padrão) gerado (texto): %s\n", tsid);
        System.out.printf("TSID (factory padrão) gerado (num): %d\n", tsid2.toLong());
        System.out.printf("TSID (factory padrão) gerado (instante): %s\n", tsid2.getInstant());
        
        System.out.println("======================================");

        // instanciando um TSID via um factory padrão que lê propriedades do ambiente alter5adas

        System.setProperty("tsid.node", "7"); // numero do nó
        System.setProperty("tsid.node.count", "32"); // tamanho do cluster


        // separando a factory porque assim possibilita ver os valores internos gerados nela via debug
        Factory factory = TSID.Factory.builder().build();

        TSID tsid3 = factory.generate();
        System.out.printf("TSID (factory local dedicada) gerado (texto): %s\n", tsid);
        System.out.printf("TSID (factory local dedicada) gerado (num): %d\n", tsid3.toLong());
        System.out.printf("TSID (factory local dedicada) gerado (instante): %s\n", tsid3.getInstant());
    }

    @Test
    public void showdGenerateTSIDWithDedicatedFactory() {
        TSID tsid = IdGenerator.generateTSID();

        System.out.printf("TSID (factory dedicada) gerado (texto): %s\n", tsid);
        System.out.printf("TSID (factory dedicada) gerado (num): %d\n", tsid.toLong());
        System.out.printf("TSID (factory dedicada) gerado (instante): %s\n", tsid.getInstant());

        Assertions.assertThat(tsid.getInstant())
        .isCloseTo(Instant.now(), Assertions.within(1, ChronoUnit.MINUTES));
    }
}
