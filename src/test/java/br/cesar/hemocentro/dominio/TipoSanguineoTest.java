package br.cesar.hemocentro.dominio;

import static br.cesar.hemocentro.dominio.TipoSanguineo.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.EnumSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

/** T3.1 — Compatibilidade ABO/Rh de concentrado de hemácias. Primeira regra testada: um erro aqui mata o paciente. */
class TipoSanguineoTest {

    @ParameterizedTest(name = "{0} doa para {1}? {2}")
    @CsvSource({
            // doador, receptor, compatível
            "O-, O-, true", "O-, AB+, true", "O-, A-, true",
            "O+, O+, true", "O+, A+, true", "O+, O-, false", "O+, AB-, false",
            "A-, A+, true", "A-, AB-, true", "A-, B-, false", "A-, O-, false",
            "A+, A+, true", "A+, AB+, true", "A+, A-, false", "A+, B+, false",
            "B-, B+, true", "B-, AB-, true", "B-, A-, false",
            "B+, AB+, true", "B+, B-, false", "B+, O+, false",
            "AB-, AB+, true", "AB-, A-, false",
            "AB+, AB+, true", "AB+, AB-, false", "AB+, O+, false"
    })
    void compatibilidadeDeHemacias(String doador, String receptor, boolean esperado) {
        assertThat(deSimbolo(doador).podeDoarPara(deSimbolo(receptor))).isEqualTo(esperado);
    }

    @Test
    @DisplayName("O- é doador universal de hemácias")
    void oNegativoDoaParaTodos() {
        for (TipoSanguineo receptor : TipoSanguineo.values()) {
            assertThat(O_NEGATIVO.podeDoarPara(receptor)).as("O- -> %s", receptor).isTrue();
        }
    }

    @Test
    @DisplayName("AB+ é receptor universal de hemácias")
    void abPositivoRecebeDeTodos() {
        for (TipoSanguineo doador : TipoSanguineo.values()) {
            assertThat(doador.podeDoarPara(AB_POSITIVO)).as("%s -> AB+", doador).isTrue();
        }
    }

    @Test
    @DisplayName("O- só recebe de O-")
    void oNegativoSoRecebeDeONegativo() {
        assertThat(EnumSet.allOf(TipoSanguineo.class).stream().filter(d -> d.podeDoarPara(O_NEGATIVO)))
                .containsExactly(O_NEGATIVO);
    }

    @ParameterizedTest
    @EnumSource(TipoSanguineo.class)
    @DisplayName("todo tipo é compatível consigo mesmo")
    void compativelConsigoMesmo(TipoSanguineo tipo) {
        assertThat(tipo.podeDoarPara(tipo)).isTrue();
    }

    @Test
    void converteSimbolo() {
        assertThat(deSimbolo("AB-")).isEqualTo(AB_NEGATIVO);
        assertThat(AB_NEGATIVO.simbolo()).isEqualTo("AB-");
        assertThatThrownBy(() -> deSimbolo("C+")).isInstanceOf(IllegalArgumentException.class);
    }
}
