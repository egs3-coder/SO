package br.cesar.hemocentro.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.cesar.hemocentro.TestcontainersConfiguration;

/** Painel web: a raiz mostra o site e a API entrega os dados que ele usa. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class PainelIT {

    @Autowired
    TestRestTemplate http;

    @Test
    @DisplayName("GET / devolve a página HTML do painel")
    void raizMostraOPainel() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.TEXT_HTML));
        ResponseEntity<String> resposta = http.exchange("/", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).contains("<title>Hemocentro");
    }

    @Test
    @DisplayName("GET /api/info continua informando a versão (usado no deploy e no rollback)")
    @SuppressWarnings("rawtypes")
    void infoTemVersao() {
        Map corpo = http.getForObject("/api/info", Map.class);

        assertThat(corpo).containsKeys("servico", "versao", "endpoints");
    }

    @Test
    @DisplayName("GET /api/resumo traz doadores, doações e bolsas utilizáveis")
    @SuppressWarnings("rawtypes")
    void resumoTemOsContadores() {
        ResponseEntity<Map> resposta = http.getForEntity("/api/resumo", Map.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).containsKeys("doadores", "doacoes", "bolsasUtilizaveis", "estoque");
    }

    @Test
    @DisplayName("GET /api/doadores-recentes devolve uma lista")
    void doadoresRecentesEhLista() {
        ResponseEntity<List> resposta = http.getForEntity("/api/doadores-recentes", List.class);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isNotNull();
    }
}
