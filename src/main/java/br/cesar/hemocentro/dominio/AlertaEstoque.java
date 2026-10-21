package br.cesar.hemocentro.dominio;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * T4.1 - Alerta de estoque critico.
 * Os tipos O tem minimo maior: O- e o doador universal (emergencias sem tipagem)
 * e O+ e o tipo mais comum da populacao.
 */
public class AlertaEstoque {

    static final int MINIMO_TIPO_O = 5;
    static final int MINIMO_DEMAIS_TIPOS = 3;

    public static int minimo(TipoSanguineo tipo) {
        return (tipo == TipoSanguineo.O_NEGATIVO || tipo == TipoSanguineo.O_POSITIVO)
                ? MINIMO_TIPO_O
                : MINIMO_DEMAIS_TIPOS;
    }

    /** Tipos abaixo do minimo. Tipo ausente no mapa conta como zero bolsas. */
    public List<TipoSanguineo> tiposCriticos(Map<TipoSanguineo, Integer> estoque) {
        return Arrays.stream(TipoSanguineo.values())
                .filter(tipo -> estoque.getOrDefault(tipo, 0) < minimo(tipo))
                .toList();
    }
}