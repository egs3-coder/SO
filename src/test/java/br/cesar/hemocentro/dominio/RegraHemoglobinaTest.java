package br.cesar.hemocentro.dominio;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** T1.7 - Hemoglobina minima: homem 13,0 g/dL; mulher 12,5 g/dL. */
class RegraHemoglobinaTest {

    private final RegraHemoglobina regra = new RegraHemoglobina();

    @Test
    @DisplayName("homem com 13,0 g/dL (no limite) esta apto")
    void homemNoLimite() {
        assertThat(regra.avaliar(Sexo.MASCULINO, 13.0)).isEmpty();
    }

    @Test
    @DisplayName("homem com 12,9 g/dL esta inapto")
    void homemAbaixo() {
        assertThat(regra.avaliar(Sexo.MASCULINO, 12.9)).contains("Hemoglobina abaixo do minimo de 13.0 g/dL");
    }

    @Test
    @DisplayName("mulher com 12,5 g/dL (no limite) esta apta")
    void mulherNoLimite() {
        assertThat(regra.avaliar(Sexo.FEMININO, 12.5)).isEmpty();
    }

    @Test
    @DisplayName("mulher com 12,4 g/dL esta inapta")
    void mulherAbaixo() {
        assertThat(regra.avaliar(Sexo.FEMININO, 12.4)).contains("Hemoglobina abaixo do minimo de 12.5 g/dL");
    }
}
