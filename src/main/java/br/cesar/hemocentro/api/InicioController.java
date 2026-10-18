package br.cesar.hemocentro.api;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Página inicial da API: mostra a versão publicada (útil para conferir deploy e rollback). */
@RestController
public class InicioController {

    private final String versao;

    public InicioController(@Value("${APP_VERSION:local}") String versao) {
        this.versao = versao;
    }

    @GetMapping("/")
    public Map<String, Object> inicio() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("servico", "API Hemocentro");
        info.put("versao", versao);
        info.put("endpoints", Map.of(
                "POST /doadores", "cadastrar doador",
                "GET /doadores/{id}/triagem", "verificar aptidão (H1)",
                "POST /doadores/{id}/doacoes", "registrar doação (H2)",
                "GET /estoque", "bolsas utilizáveis por tipo",
                "POST /transfusoes", "atender transfusão (H3)",
                "GET /actuator/health", "saúde da aplicação"));
        return info;
    }
}
