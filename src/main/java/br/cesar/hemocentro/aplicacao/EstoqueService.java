package br.cesar.hemocentro.aplicacao;

import java.time.Clock;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.cesar.hemocentro.dominio.BolsaDisponivel;
import br.cesar.hemocentro.dominio.SelecaoBolsas;
import br.cesar.hemocentro.dominio.TipoSanguineo;
import br.cesar.hemocentro.modelo.Bolsa;
import br.cesar.hemocentro.modelo.BolsaRepository;
import br.cesar.hemocentro.modelo.StatusBolsa;

/** Estoque utilizável e H3 — atendimento de transfusões. */
@Service
public class EstoqueService {

    private final BolsaRepository bolsas;
    private final Clock clock;
    private final SelecaoBolsas selecao = new SelecaoBolsas();

    public EstoqueService(BolsaRepository bolsas, Clock clock) {
        this.bolsas = bolsas;
        this.clock = clock;
    }

    /** Quantidade de bolsas utilizáveis (disponíveis e dentro da validade) por tipo, com todos os tipos listados. */
    @Transactional(readOnly = true)
    public Map<String, Integer> resumo() {
        Map<String, Integer> porTipo = new LinkedHashMap<>();
        for (TipoSanguineo tipo : TipoSanguineo.values()) {
            porTipo.put(tipo.simbolo(), 0);
        }
        for (Bolsa b : bolsas.findByStatusAndValidadeGreaterThanEqual(StatusBolsa.DISPONIVEL, hoje())) {
            porTipo.merge(b.getTipoSanguineo().simbolo(), 1, Integer::sum);
        }
        return porTipo;
    }

    /**
     * Escolhe as bolsas pela regra de domínio e marca como utilizadas na mesma transação.
     * As linhas ficam travadas até o commit, então duas solicitações simultâneas não levam a mesma bolsa.
     */
    @Transactional
    public List<Bolsa> atenderTransfusao(TipoSanguineo receptor, int quantidade) {
        List<Bolsa> estoque = bolsas.travarDisponiveis(StatusBolsa.DISPONIVEL, hoje());
        Map<Long, Bolsa> porId = new LinkedHashMap<>();
        estoque.forEach(b -> porId.put(b.getId(), b));

        List<BolsaDisponivel> escolhidas = selecao.selecionar(
                receptor, quantidade, estoque.stream().map(Bolsa::paraSelecao).toList(), hoje());

        List<Bolsa> entregues = escolhidas.stream().map(e -> porId.get(e.id())).toList();
        entregues.forEach(Bolsa::marcarUtilizada);
        return entregues;
    }

    private LocalDate hoje() {
        return LocalDate.now(clock);
    }
}
