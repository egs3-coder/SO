package br.cesar.hemocentro.aplicacao;

import java.time.Clock;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.cesar.hemocentro.modelo.Bolsa;
import br.cesar.hemocentro.modelo.BolsaRepository;
import br.cesar.hemocentro.modelo.Doacao;
import br.cesar.hemocentro.modelo.DoacaoRepository;

/** H2 — Registrar doação: só coleta se a triagem (H1) aprovar, e gera a bolsa com validade (T2.1). */
@Service
public class DoacaoService {

    private final DoadorService doadorService;
    private final DoacaoRepository doacoes;
    private final BolsaRepository bolsas;
    private final Clock clock;

    public DoacaoService(DoadorService doadorService, DoacaoRepository doacoes, BolsaRepository bolsas, Clock clock) {
        this.doadorService = doadorService;
        this.doacoes = doacoes;
        this.bolsas = bolsas;
        this.clock = clock;
    }

    @Transactional
    public Doacao registrar(Long doadorId) {
        var doador = doadorService.buscar(doadorId);
        var triagem = doadorService.triar(doador);
        if (!triagem.apto()) {
            throw new DoadorInaptoException(triagem.motivos());
        }

        LocalDate hoje = LocalDate.now(clock);
        var bolsa = bolsas.save(Bolsa.coletada(doador.getTipoSanguineo(), hoje));
        return doacoes.save(new Doacao(doador, hoje, bolsa));
    }
}
