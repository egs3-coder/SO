package br.cesar.hemocentro.api;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.cesar.hemocentro.api.Dtos.DoacaoResponse;
import br.cesar.hemocentro.api.Dtos.DoadorResponse;
import br.cesar.hemocentro.api.Dtos.NovoDoadorRequest;
import br.cesar.hemocentro.aplicacao.DoacaoService;
import br.cesar.hemocentro.aplicacao.DoadorService;
import br.cesar.hemocentro.dominio.ResultadoTriagem;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/doadores")
public class DoadorController {

    private final DoadorService doadorService;
    private final DoacaoService doacaoService;

    public DoadorController(DoadorService doadorService, DoacaoService doacaoService) {
        this.doadorService = doadorService;
        this.doacaoService = doacaoService;
    }

    @PostMapping
    public ResponseEntity<DoadorResponse> cadastrar(@Valid @RequestBody NovoDoadorRequest request) {
        var doador = doadorService.cadastrar(request.paraEntidade());
        return ResponseEntity.created(URI.create("/doadores/" + doador.getId())).body(DoadorResponse.de(doador));
    }

    @GetMapping("/{id}")
    public DoadorResponse buscar(@PathVariable Long id) {
        return DoadorResponse.de(doadorService.buscar(id));
    }

    /** H1 — triagem: sempre 200; o corpo diz se está apto e por quê. */
    @GetMapping("/{id}/triagem")
    public ResultadoTriagem triagem(@PathVariable Long id) {
        return doadorService.triar(id);
    }

    /** H2 — registra doação; 422 com os motivos se o doador estiver inapto. */
    @PostMapping("/{id}/doacoes")
    public ResponseEntity<DoacaoResponse> doar(@PathVariable Long id) {
        var doacao = doacaoService.registrar(id);
        return ResponseEntity.created(URI.create("/doacoes/" + doacao.getId())).body(DoacaoResponse.de(doacao));
    }
}
