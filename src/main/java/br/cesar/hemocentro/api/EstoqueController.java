package br.cesar.hemocentro.api;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.cesar.hemocentro.api.Dtos.BolsaResponse;
import br.cesar.hemocentro.api.Dtos.TransfusaoRequest;
import br.cesar.hemocentro.api.Dtos.TransfusaoResponse;
import br.cesar.hemocentro.aplicacao.EstoqueService;
import br.cesar.hemocentro.dominio.TipoSanguineo;
import jakarta.validation.Valid;

@RestController
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    /** Bolsas utilizáveis por tipo sanguíneo. */
    @GetMapping("/estoque")
    public Map<String, Integer> estoque() {
        return estoqueService.resumo();
    }

    /** H3 — atende solicitação de transfusão; 409 se o estoque compatível não bastar. */
    @PostMapping("/transfusoes")
    @ResponseStatus(HttpStatus.CREATED)
    public TransfusaoResponse transfusao(@Valid @RequestBody TransfusaoRequest request) {
        var receptor = TipoSanguineo.deSimbolo(request.tipoReceptor());
        var entregues = estoqueService.atenderTransfusao(receptor, request.quantidade());
        return new TransfusaoResponse(receptor.simbolo(), entregues.stream().map(BolsaResponse::de).toList());
    }
}
