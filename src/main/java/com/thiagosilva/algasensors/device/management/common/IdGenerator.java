package com.thiagosilva.algasensors.device.management.common;

import java.util.Optional;

import io.hypersistence.tsid.TSID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdGenerator {

    private static final TSID.Factory tsidFactory;

    // inicializador estático
    static {

        // seta as propriedades do sistema necessárias para o tsid,
        // buscando de variáveis de ambiente, e atribuindo, se existirem
        // se existir, seta o padrão das propriedades do sistema caso contrário
        // o padrão é 1024 para o node count e um valor aleatório para o node

        // numero do nó
        Optional.ofNullable(System.getenv("tsid.node"))
        .ifPresent(v -> System.setProperty("tsid.node", v));

        // tamanho do cluster
        Optional.ofNullable(System.getenv("tsid.node.count"))
        .ifPresent(v -> System.setProperty("tsid.node.count", v));


        // System.setProperty("tsid.node", "7");
        // System.setProperty("tsid.node.count", "32");

        tsidFactory = TSID.Factory.builder().build();
    }

    public static TSID generateTSID() {
        return tsidFactory.generate();
    }

}
