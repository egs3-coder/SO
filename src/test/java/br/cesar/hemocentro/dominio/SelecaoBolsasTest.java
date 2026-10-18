package br.cesar.hemocentro.dominio;

import static br.cesar.hemocentro.dominio.TipoSanguineo.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** T3.2 e T3.3 — Escolha das bolsas para uma transfusão. */
class SelecaoBolsasTest {

    private static final LocalDate HOJE = LocalDate.of(2026, 10, 18);

    private final SelecaoBolsas selecao = new SelecaoBolsas();

    private static BolsaDisponivel bolsa(long id, TipoSanguineo tipo, int diasParaVencer) {
        return new BolsaDisponivel(id, tipo, HOJE.plusDays(diasParaVencer));
    }

    @Test
    @DisplayName("T3.1 aplicada: nunca entrega bolsa incompatível")
    void ignoraIncompativeis() {
        var estoque = List.of(bolsa(1, A_POSITIVO, 10), bolsa(2, B_POSITIVO, 10), bolsa(3, O_NEGATIVO, 10));

        var escolhidas = selecao.selecionar(O_NEGATIVO, 1, estoque, HOJE);

        assertThat(escolhidas).extracting(BolsaDisponivel::id).containsExactly(3L);
    }

    @Test
    @DisplayName("T3.2: bolsa vencida nunca é entregue")
    void ignoraVencidas() {
        var estoque = List.of(bolsa(1, A_POSITIVO, -1), bolsa(2, A_POSITIVO, 5));

        var escolhidas = selecao.selecionar(A_POSITIVO, 1, estoque, HOJE);

        assertThat(escolhidas).extracting(BolsaDisponivel::id).containsExactly(2L);
    }

    @Test
    @DisplayName("T3.2: bolsa que vence hoje ainda pode ser usada")
    void venceHojeAindaServe() {
        var escolhidas = selecao.selecionar(A_POSITIVO, 1, List.of(bolsa(1, A_POSITIVO, 0)), HOJE);

        assertThat(escolhidas).extracting(BolsaDisponivel::id).containsExactly(1L);
    }

    @Test
    @DisplayName("T3.3: prioriza o mesmo tipo do receptor, preservando O- para quem só pode receber O-")
    void priorizaIsogrupo() {
        var estoque = List.of(bolsa(1, O_NEGATIVO, 2), bolsa(2, A_POSITIVO, 30));

        var escolhidas = selecao.selecionar(A_POSITIVO, 1, estoque, HOJE);

        assertThat(escolhidas).extracting(BolsaDisponivel::id).containsExactly(2L);
    }

    @Test
    @DisplayName("T3.3: dentro do mesmo tipo, entrega primeiro a que vence antes (FEFO)")
    void fefo() {
        var estoque = List.of(bolsa(1, B_NEGATIVO, 20), bolsa(2, B_NEGATIVO, 3), bolsa(3, B_NEGATIVO, 10));

        var escolhidas = selecao.selecionar(B_NEGATIVO, 2, estoque, HOJE);

        assertThat(escolhidas).extracting(BolsaDisponivel::id).containsExactly(2L, 3L);
    }

    @Test
    @DisplayName("T3.3: completa com tipos compatíveis quando o isogrupo não basta")
    void completaComCompativeis() {
        var estoque = List.of(bolsa(1, AB_POSITIVO, 10), bolsa(2, O_NEGATIVO, 8), bolsa(3, A_NEGATIVO, 4));

        var escolhidas = selecao.selecionar(AB_POSITIVO, 3, estoque, HOJE);

        assertThat(escolhidas).extracting(BolsaDisponivel::id).containsExactly(1L, 3L, 2L);
    }

    @Test
    @DisplayName("T3.4: sem bolsas compatíveis suficientes, recusa em vez de entregar parcial")
    void estoqueInsuficiente() {
        var estoque = List.of(bolsa(1, O_NEGATIVO, 10), bolsa(2, O_POSITIVO, 10), bolsa(3, O_NEGATIVO, -2));

        assertThatThrownBy(() -> selecao.selecionar(O_NEGATIVO, 2, estoque, HOJE))
                .isInstanceOf(EstoqueInsuficienteException.class)
                .hasMessageContaining("O-")
                .satisfies(e -> {
                    var ex = (EstoqueInsuficienteException) e;
                    assertThat(ex.getSolicitadas()).isEqualTo(2);
                    assertThat(ex.getDisponiveis()).isEqualTo(1);
                });
    }

    @Test
    @DisplayName("quantidade precisa ser ao menos 1")
    void quantidadeInvalida() {
        assertThatThrownBy(() -> selecao.selecionar(A_POSITIVO, 0, List.of(), HOJE))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
