package br.cesar.hemocentro.dominio;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** T1.6 - Tatuagem ou maquiagem definitiva ha menos de 12 meses deixa o doador inapto. */
class RegraTatuagemTest {

    private static final LocalDate HOJE = LocalDate.of(2026, 10, 22);
    private static final String MOTIVO = "Tatuagem ou maquiagem definitiva ha menos de 12 meses";

    private final RegraTatuagem regra = new RegraTatuagem();

    @Test
    @DisplayName("quem nunca fez tatuagem esta apto")
    void semTatuagem() {
        assertThat(regra.avaliar(null, HOJE)).isEmpty();
    }

    @Test
    @DisplayName("tatuagem feita ha 11 meses: inapto")
    void onzeMeses() {
        assertThat(regra.avaliar(HOJE.minusMonths(11), HOJE)).contains(MOTIVO);
    }

    @Test
    @DisplayName("tatuagem feita ontem: inapto")
    void ontem() {
        assertThat(regra.avaliar(HOJE.minusDays(1), HOJE)).contains(MOTIVO);
    }

    @Test
    @DisplayName("tatuagem feita ha exatamente 12 meses: ja pode doar")
    void dozeMeses() {
        assertThat(regra.avaliar(HOJE.minusMonths(12), HOJE)).isEmpty();
    }
}
