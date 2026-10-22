package br.cesar.hemocentro.dominio;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** T1.8 - Frequencia cardiaca precisa estar entre 50 e 100 batimentos por minuto. */
class RegraFrequenciaCardiacaTest {

    private static final String MOTIVO = "Frequencia cardiaca fora do intervalo de 50 a 100 bpm";

    private final RegraFrequenciaCardiaca regra = new RegraFrequenciaCardiaca();

    @Test
    @DisplayName("49 bpm (abaixo do minimo): inapto")
    void abaixoDoMinimo() {
        assertThat(regra.avaliar(49)).contains(MOTIVO);
    }

    @Test
    @DisplayName("50 bpm (no limite minimo): apto")
    void noMinimo() {
        assertThat(regra.avaliar(50)).isEmpty();
    }

    @Test
    @DisplayName("100 bpm (no limite maximo): apto")
    void noMaximo() {
        assertThat(regra.avaliar(100)).isEmpty();
    }

    @Test
    @DisplayName("101 bpm (acima do maximo): inapto")
    void acimaDoMaximo() {
        assertThat(regra.avaliar(101)).contains(MOTIVO);
    }
}
