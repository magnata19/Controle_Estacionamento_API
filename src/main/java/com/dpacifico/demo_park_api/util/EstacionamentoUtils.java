package com.dpacifico.demo_park_api.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstacionamentoUtils {

    //2019-01-21T05:47:08.644 -> hora do cadastro
    //20190121-054708 -> numero do recibo

    public static String gerarRecibo() {

        LocalDateTime date = LocalDateTime.now();
        String recibo = date.toString().substring(0,19);
        return recibo.replace("-","")
                .replace(":","")
                .replace("T","-");
    }
}
