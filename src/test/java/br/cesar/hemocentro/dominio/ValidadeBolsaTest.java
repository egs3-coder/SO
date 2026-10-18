package br.cesar.hemocentro.dominio;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** T2.1 — Validade do concentrado de hemácias (anticoagulante CPDA-1: 35 dias). */
class ValidadeBolsaTest {

    private static final LocalDate COLETA = LocalDate.of(2026, 10, 1);

    @Test
    @DisplayName("bolsa coletada vale 35 dias")
    void validadeTrintaECincoDias() {
        assertThat(ValidadeBolsa.calcular(COLETA)).isEqualTo(LocalDate.of(2026, 11, 5));
    }

    @Test
    @DisplayName("no último dia de validade a bolsa ainda pode ser usada")
    void ultimoDiaAindaValida() {
        LocalDate validade = ValidadeBolsa.calcular(COLETA);
        assertThat(ValidadeBolsa.vencida(validade, validade)).isFalse();
    }

    @Test
    @DisplayName("um dia depois da validade a bolsa está vencida")
    void diaSeguinteVencida() {
        LocalDate validade = ValidadeBolsa.calcular(COLETA);
        assertThat(ValidadeBolsa.vencida(validade, validade.plusDays(1))).isTrue();
    }
}
