package br.cesar.hemocentro.dominio;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** T1.9 - Pressao arterial: sistolica ate 180 mmHg e diastolica ate 100 mmHg. */
class RegraPressaoArterialTest {

    private static final String SISTOLICA = "Pressao sistolica acima de 180 mmHg";
    private static final String DIASTOLICA = "Pressao diastolica acima de 100 mmHg";

    private final RegraPressaoArterial regra = new RegraPressaoArterial();

    @Test
    @DisplayName("180 x 100 (nos limites): apto")
    void noLimite() {
        assertThat(regra.avaliar(180, 100)).isEmpty();
    }

    @Test
    @DisplayName("181 x 80: inapto pela sistolica")
    void sistolicaAlta() {
        assertThat(regra.avaliar(181, 80)).containsExactly(SISTOLICA);
    }

    @Test
    @DisplayName("120 x 101: inapto pela diastolica")
    void diastolicaAlta() {
        assertThat(regra.avaliar(120, 101)).containsExactly(DIASTOLICA);
    }

    @Test
    @DisplayName("190 x 110: os dois motivos aparecem")
    void asDuasAltas() {
        assertThat(regra.avaliar(190, 110)).containsExactly(SISTOLICA, DIASTOLICA);
    }
}
