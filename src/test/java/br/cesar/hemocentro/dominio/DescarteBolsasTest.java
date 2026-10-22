package br.cesar.hemocentro.dominio;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** T4.2 - Bolsas vencidas devem ser separadas para descarte. */
class DescarteBolsasTest {

    private static final LocalDate HOJE = LocalDate.of(2026, 10, 22);

    private final DescarteBolsas descarte = new DescarteBolsas();

    private static BolsaDisponivel bolsa(long id, int diasParaVencer) {
        return new BolsaDisponivel(id, TipoSanguineo.A_POSITIVO, HOJE.plusDays(diasParaVencer));
    }

    @Test
    @DisplayName("bolsa que venceu ontem vai para descarte")
    void vencidaOntem() {
        assertThat(descarte.paraDescartar(List.of(bolsa(1, -1)), HOJE)).containsExactly(1L);
    }

    @Test
    @DisplayName("bolsa que vence hoje ainda pode ser usada: nao descarta")
    void venceHoje() {
        assertThat(descarte.paraDescartar(List.of(bolsa(1, 0)), HOJE)).isEmpty();
    }

    @Test
    @DisplayName("estoque misturado: so as vencidas, na ordem do estoque")
    void estoqueMisturado() {
        var estoque = List.of(bolsa(1, 10), bolsa(2, -3), bolsa(3, 0), bolsa(4, -40));
        assertThat(descarte.paraDescartar(estoque, HOJE)).containsExactly(2L, 4L);
    }

    @Test
    @DisplayName("estoque vazio: nada a descartar")
    void estoqueVazio() {
        assertThat(descarte.paraDescartar(List.of(), HOJE)).isEmpty();
    }
}
