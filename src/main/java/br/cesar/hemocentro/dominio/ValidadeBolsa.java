package br.cesar.hemocentro.dominio;

import java.time.LocalDate;

/** Validade do concentrado de hemácias coletado com anticoagulante CPDA-1. */
public final class ValidadeBolsa {

    public static final int DIAS_VALIDADE = 35;

    private ValidadeBolsa() {
    }

    public static LocalDate calcular(LocalDate dataColeta) {
        return dataColeta.plusDays(DIAS_VALIDADE);
    }

    /** A bolsa pode ser usada até o fim do dia da validade, inclusive. */
    public static boolean vencida(LocalDate validade, LocalDate hoje) {
        return hoje.isAfter(validade);
    }
}
