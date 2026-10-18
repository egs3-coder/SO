package br.cesar.hemocentro.dominio;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Escolhe quais bolsas do estoque atendem uma solicitação de transfusão.
 *
 * <ol>
 *   <li>só bolsas compatíveis (ABO/Rh) e não vencidas;</li>
 *   <li>primeiro o mesmo tipo do receptor (isogrupo), para não gastar O- à toa;</li>
 *   <li>depois a que vence antes (FEFO - first expired, first out), para reduzir descarte.</li>
 * </ol>
 */
public class SelecaoBolsas {

    public List<BolsaDisponivel> selecionar(TipoSanguineo receptor, int quantidade,
                                            List<BolsaDisponivel> estoque, LocalDate hoje) {
        if (quantidade < 1) {
            throw new IllegalArgumentException("A quantidade de bolsas deve ser ao menos 1");
        }

        Comparator<BolsaDisponivel> prioridade = Comparator
                .comparing((BolsaDisponivel b) -> b.tipo() == receptor ? 0 : 1)
                .thenComparing(BolsaDisponivel::validade)
                .thenComparing(BolsaDisponivel::id);

        return estoque.stream()
                .filter(b -> b.tipo().podeDoarPara(receptor))
                .filter(b -> !ValidadeBolsa.vencida(b.validade(), hoje))
                .sorted(prioridade)
                .limit(quantidade)
                .toList();
    }
}
