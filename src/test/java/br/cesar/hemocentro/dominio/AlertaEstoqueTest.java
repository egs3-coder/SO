package br.cesar.hemocentro.dominio;

import static br.cesar.hemocentro.dominio.TipoSanguineo.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** T4.1 - Alerta de estoque critico. */
class AlertaEstoqueTest {

    private final AlertaEstoque alerta = new AlertaEstoque();

    private static Map<TipoSanguineo, Integer> estoqueCheio() {
        Map<TipoSanguineo, Integer> estoque = new EnumMap<>(TipoSanguineo.class);
        for (TipoSanguineo tipo : TipoSanguineo.values()) {
            estoque.put(tipo, 10);
        }
        return estoque;
    }

    @Test
    @DisplayName("estoque cheio nao gera alerta")
    void semAlerta() {
        assertThat(alerta.tiposCriticos(estoqueCheio())).isEmpty();
    }

    @Test
    @DisplayName("tipo O tem minimo de 5 bolsas: 4 e critico, 5 nao e")
    void minimoDoTipoO() {
        var estoque = estoqueCheio();
        estoque.put(O_NEGATIVO, 4);
        estoque.put(O_POSITIVO, 5);
        assertThat(alerta.tiposCriticos(estoque)).containsExactly(O_NEGATIVO);
    }

    @Test
    @DisplayName("demais tipos tem minimo de 3 bolsas: 2 e critico, 3 nao e")
    void minimoDosDemaisTipos() {
        var estoque = estoqueCheio();
        estoque.put(AB_NEGATIVO, 2);
        estoque.put(B_POSITIVO, 3);
        assertThat(alerta.tiposCriticos(estoque)).containsExactly(AB_NEGATIVO);
    }

    @Test
    @DisplayName("tipo ausente no estoque conta como zero bolsas")
    void tipoAusenteEhCritico() {
        var estoque = estoqueCheio();
        estoque.remove(A_POSITIVO);
        assertThat(alerta.tiposCriticos(estoque)).containsExactly(A_POSITIVO);
    }
}
