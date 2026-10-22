package br.cesar.hemocentro.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.cesar.hemocentro.api.Dtos.DoadorResponse;
import br.cesar.hemocentro.aplicacao.EstoqueService;
import br.cesar.hemocentro.modelo.DoacaoRepository;
import br.cesar.hemocentro.modelo.DoadorRepository;

/** Dados de leitura para o painel web (página inicial). */
@RestController
@RequestMapping("/api")
public class PainelController {

    private final DoadorRepository doadores;
    private final DoacaoRepository doacoes;
    private final EstoqueService estoque;

    public PainelController(DoadorRepository doadores, DoacaoRepository doacoes, EstoqueService estoque) {
        this.doadores = doadores;
        this.doacoes = doacoes;
        this.estoque = estoque;
    }

    /** Contadores da visão geral: doadores, doações e bolsas utilizáveis por tipo. */
    @GetMapping("/resumo")
    @Transactional(readOnly = true)
    public Map<String, Object> resumo() {
        Map<String, Integer> porTipo = estoque.resumo();
        int bolsas = porTipo.values().stream().mapToInt(Integer::intValue).sum();
        Map<String, Object> resumo = new LinkedHashMap<>();
        resumo.put("doadores", doadores.count());
        resumo.put("doacoes", doacoes.count());
        resumo.put("bolsasUtilizaveis", bolsas);
        resumo.put("estoque", porTipo);
        return resumo;
    }

    /** Os 10 doadores cadastrados mais recentemente. */
    @GetMapping("/doadores-recentes")
    @Transactional(readOnly = true)
    public List<DoadorResponse> doadoresRecentes() {
        return doadores.findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id")))
                .map(DoadorResponse::de)
                .getContent();
    }
}
