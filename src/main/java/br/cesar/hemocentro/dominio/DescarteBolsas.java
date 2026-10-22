package br.cesar.hemocentro.dominio;

import java.time.LocalDate;
import java.util.List;

/**
 * T4.2 - Descarte de bolsas vencidas.
 * Reaproveita a regra ValidadeBolsa.vencida (T2.1): a bolsa vale ate o ultimo dia, inclusive.
 */
public class DescarteBolsas {

    /** Ids das bolsas vencidas, na mesma ordem em que aparecem no estoque. */
    public List<Long> paraDescartar(List<BolsaDisponivel> estoque, LocalDate hoje) {
        return estoque.stream()
                .filter(bolsa -> ValidadeBolsa.vencida(bolsa.validade(), hoje))
                .map(BolsaDisponivel::id)
                .toList();
    }
}
