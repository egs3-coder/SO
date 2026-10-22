package br.cesar.hemocentro.api;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Informacoes da API: versao publicada (SHA do commit) e endpoints.
 * A raiz "/" agora mostra o painel web (static/index.html); este JSON fica em /api/info,
 * que e onde a esteira confere se o deploy e o rollback aconteceram.
 */
@RestController
public class InicioController {

    private final String versao;

    public InicioController(@Value("${APP_VERSION:local}") String versao) {
        this.versao = versao;
    }

    @GetMapping("/api/info")
    public Map<String, Object> info() {
        Map<String, Object> endpoints = new LinkedHashMap<>();
        endpoints.put("GET /", "painel web do hemocentro");
        endpoints.put("POST /doadores", "cadastrar doador");
        endpoints.put("GET /doadores/{id}", "consultar doador");
        endpoints.put("GET /doadores/{id}/triagem", "verificar aptidao (H1)");
        endpoints.put("POST /doadores/{id}/doacoes", "registrar doacao (H2)");
        endpoints.put("GET /estoque", "bolsas utilizaveis por tipo");
        endpoints.put("GET /estoque/criticos", "tipos abaixo do estoque minimo (T4.1)");
        endpoints.put("POST /transfusoes", "atender transfusao (H3)");
        endpoints.put("GET /api/resumo", "contadores do painel");
        endpoints.put("GET /api/doadores-recentes", "ultimos doadores cadastrados");
        endpoints.put("GET /actuator/health", "saude da aplicacao");

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("servico", "API Hemocentro");
        info.put("versao", versao);
        info.put("endpoints", endpoints);
        return info;
    }
}
