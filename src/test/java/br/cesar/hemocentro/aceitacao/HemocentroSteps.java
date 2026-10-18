package br.cesar.hemocentro.aceitacao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.cesar.hemocentro.dominio.TipoSanguineo;
import br.cesar.hemocentro.modelo.Bolsa;
import br.cesar.hemocentro.modelo.BolsaRepository;
import br.cesar.hemocentro.modelo.Doacao;
import br.cesar.hemocentro.modelo.DoacaoRepository;
import br.cesar.hemocentro.modelo.DoadorRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

/** Passos dos cenários BDD: falam com a API por HTTP, como um cliente de verdade. */
public class HemocentroSteps {

    private static final AtomicLong SEQUENCIA_CPF = new AtomicLong(10_000_000_000L);

    @Autowired
    private TestRestTemplate http;

    @Autowired
    private Clock clock;

    @Autowired
    private DoadorRepository doadores;

    @Autowired
    private DoacaoRepository doacoes;

    @Autowired
    private BolsaRepository bolsas;

    private final Map<String, Long> idPorNome = new HashMap<>();
    private ResponseEntity<Map<String, Object>> ultimaResposta;

    private static final ParameterizedTypeReference<Map<String, Object>> JSON = new ParameterizedTypeReference<>() {
    };

    @Before
    public void limparBanco() {
        doacoes.deleteAll();
        bolsas.deleteAll();
        doadores.deleteAll();
        idPorNome.clear();
    }

    private LocalDate hoje() {
        return LocalDate.now(clock);
    }

    // ---------- Dado ----------

    @Dado("um doador {string} do sexo {string} com {int} anos, {int} kg e tipo {string}")
    public void umDoador(String nome, String sexo, int idade, int peso, String tipo) {
        Map<String, Object> corpo = Map.of(
                "nome", nome,
                "cpf", String.valueOf(SEQUENCIA_CPF.incrementAndGet()),
                "dataNascimento", hoje().minusYears(idade).minusDays(1).toString(),
                "sexo", sexo,
                "pesoKg", peso,
                "tipoSanguineo", tipo);

        var resposta = http.exchange("/doadores", HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(corpo), JSON);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        idPorNome.put(nome, ((Number) resposta.getBody().get("id")).longValue());
    }

    @Dado("{string} doou sangue há {int} dias")
    public void doouHaDias(String nome, int dias) {
        var doador = doadores.findById(idPorNome.get(nome)).orElseThrow();
        LocalDate data = hoje().minusDays(dias);
        var bolsa = bolsas.save(Bolsa.coletada(doador.getTipoSanguineo(), data));
        doacoes.save(new Doacao(doador, data, bolsa));
    }

    @Dado("o estoque possui as bolsas:")
    public void estoquePossui(DataTable tabela) {
        for (Map<String, String> linha : tabela.asMaps()) {
            var tipo = TipoSanguineo.deSimbolo(linha.get("tipo"));
            int venceEm = Integer.parseInt(linha.get("vence_em_dias"));
            // coleta calculada para a validade cair exatamente em hoje + venceEm
            LocalDate coleta = hoje().plusDays(venceEm).minusDays(35);
            bolsas.save(Bolsa.coletada(tipo, coleta));
        }
    }

    // ---------- Quando ----------

    @Quando("consulto a triagem de {string}")
    public void consultoTriagem(String nome) {
        ultimaResposta = http.exchange("/doadores/" + idPorNome.get(nome) + "/triagem", HttpMethod.GET, null, JSON);
    }

    @Quando("{string} doa sangue hoje")
    public void doaHoje(String nome) {
        ultimaResposta = http.exchange("/doadores/" + idPorNome.get(nome) + "/doacoes", HttpMethod.POST, null, JSON);
    }

    @Quando("o médico solicita {int} bolsa(s) para um receptor {string}")
    public void solicitaBolsas(int quantidade, String receptor) {
        Map<String, Object> corpo = Map.of("tipoReceptor", receptor, "quantidade", quantidade);
        ultimaResposta = http.exchange("/transfusoes", HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(corpo), JSON);
    }

    // ---------- Então ----------

    @Então("o doador está apto")
    public void doadorApto() {
        assertThat(ultimaResposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ultimaResposta.getBody().get("apto")).isEqualTo(true);
    }

    @Então("o doador está inapto com o motivo {string}")
    public void doadorInapto(String motivo) {
        assertThat(ultimaResposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ultimaResposta.getBody().get("apto")).isEqualTo(false);
        assertThat((List<?>) ultimaResposta.getBody().get("motivos")).contains(motivo);
    }

    @Então("a doação é registrada com bolsa {string} válida por {int} dias")
    public void doacaoRegistrada(String tipo, int dias) {
        assertThat(ultimaResposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var corpo = ultimaResposta.getBody();
        assertThat(corpo.get("tipoSanguineo")).isEqualTo(tipo);
        assertThat(corpo.get("validade")).isEqualTo(hoje().plusDays(dias).toString());
    }

    @Então("a doação é recusada com o motivo {string}")
    public void doacaoRecusada(String motivo) {
        assertThat(ultimaResposta.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat((List<?>) ultimaResposta.getBody().get("motivos")).contains(motivo);
    }

    @Então("o estoque tem {int} bolsa(s) {string} disponível(is)")
    public void estoqueTem(int quantidade, String tipo) {
        assertThat(estoque().getOrDefault(tipo, 0)).isEqualTo(quantidade);
    }

    @Então("a transfusão é atendida com {int} bolsa(s) do tipo {string}")
    public void transfusaoAtendida(int quantidade, String tipo) {
        assertThat(ultimaResposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        List<Map<String, Object>> entregues = bolsasEntregues();
        assertThat(entregues).hasSize(quantidade);
        assertThat(entregues).allSatisfy(b -> assertThat(b.get("tipoSanguineo")).isEqualTo(tipo));
    }

    @Então("a bolsa entregue vence em {int} dias")
    public void bolsaEntregueVenceEm(int dias) {
        assertThat(bolsasEntregues().get(0).get("validade")).isEqualTo(hoje().plusDays(dias).toString());
    }

    @Então("a solicitação é recusada por estoque insuficiente")
    public void recusadaPorEstoque() {
        assertThat(ultimaResposta.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Então("restam {int} bolsas utilizáveis no estoque")
    public void restamBolsas(int quantidade) {
        assertThat(estoque().values().stream().mapToInt(Integer::intValue).sum()).isEqualTo(quantidade);
    }

    // ---------- auxiliares ----------

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> bolsasEntregues() {
        return (List<Map<String, Object>>) ultimaResposta.getBody().get("bolsas");
    }

    private Map<String, Integer> estoque() {
        var resposta = http.exchange("/estoque", HttpMethod.GET, null,
                new ParameterizedTypeReference<Map<String, Integer>>() {
                });
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        return resposta.getBody();
    }
}
