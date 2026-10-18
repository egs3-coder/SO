package br.cesar.hemocentro.aplicacao;

import java.time.Clock;
import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.cesar.hemocentro.dominio.ResultadoTriagem;
import br.cesar.hemocentro.dominio.TriagemDoador;
import br.cesar.hemocentro.modelo.DoacaoRepository;
import br.cesar.hemocentro.modelo.Doador;
import br.cesar.hemocentro.modelo.DoadorRepository;

@Service
public class DoadorService {

    private final DoadorRepository doadores;
    private final DoacaoRepository doacoes;
    private final Clock clock;
    private final TriagemDoador triagem = new TriagemDoador();

    public DoadorService(DoadorRepository doadores, DoacaoRepository doacoes, Clock clock) {
        this.doadores = doadores;
        this.doacoes = doacoes;
        this.clock = clock;
    }

    @Transactional
    public Doador cadastrar(Doador novo) {
        if (doadores.existsByCpf(novo.getCpf())) {
            throw new CpfJaCadastradoException(novo.getCpf());
        }
        return doadores.save(novo);
    }

    @Transactional(readOnly = true)
    public Doador buscar(Long id) {
        return doadores.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Doador " + id + " não encontrado"));
    }

    @Transactional(readOnly = true)
    public ResultadoTriagem triar(Long id) {
        return triar(buscar(id));
    }

    ResultadoTriagem triar(Doador doador) {
        return triagem.avaliar(doador.perfil(), doacoes.datasDeColetaDoDoador(doador.getId()), LocalDate.now(clock));
    }
}
