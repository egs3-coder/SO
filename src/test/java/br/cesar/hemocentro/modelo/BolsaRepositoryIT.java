package br.cesar.hemocentro.modelo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import br.cesar.hemocentro.TestcontainersConfiguration;
import br.cesar.hemocentro.dominio.Sexo;
import br.cesar.hemocentro.dominio.TipoSanguineo;

/**
 * T2.3 / T3.5 — Teste de integração com o PostgreSQL real (Testcontainers + migrations Flyway).
 * Garante que o schema versionado bate com as entidades e que as consultas filtram o estoque certo.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class BolsaRepositoryIT {

    static final LocalDate HOJE = LocalDate.of(2026, 10, 18);

    @Autowired
    BolsaRepository bolsas;

    @Autowired
    DoadorRepository doadores;

    @Autowired
    DoacaoRepository doacoes;

    @Test
    @DisplayName("estoque utilizável = só bolsas DISPONIVEL e dentro da validade")
    void buscaApenasDisponiveisNaoVencidas() {
        var valida = bolsas.save(Bolsa.coletada(TipoSanguineo.A_POSITIVO, HOJE.minusDays(10)));
        var venceHoje = bolsas.save(Bolsa.coletada(TipoSanguineo.O_NEGATIVO, HOJE.minusDays(35)));
        bolsas.save(Bolsa.coletada(TipoSanguineo.B_POSITIVO, HOJE.minusDays(36)));          // vencida
        var utilizada = Bolsa.coletada(TipoSanguineo.A_POSITIVO, HOJE.minusDays(1));
        utilizada.marcarUtilizada();
        bolsas.save(utilizada);

        var encontradas = bolsas.findByStatusAndValidadeGreaterThanEqual(StatusBolsa.DISPONIVEL, HOJE);

        assertThat(encontradas).extracting(Bolsa::getId)
                .containsExactlyInAnyOrder(valida.getId(), venceHoje.getId());
    }

    @Test
    @DisplayName("doação persiste vinculada ao doador e à bolsa, e o histórico volta como datas")
    void persisteDoacaoComBolsa() {
        var doador = doadores.save(new Doador("Maria Silva", "12345678901", LocalDate.of(1995, 3, 10),
                Sexo.FEMININO, 62.5, TipoSanguineo.O_NEGATIVO, false));
        var bolsa = bolsas.save(Bolsa.coletada(doador.getTipoSanguineo(), HOJE));
        doacoes.save(new Doacao(doador, HOJE, bolsa));
        doacoes.save(new Doacao(doador, HOJE.minusDays(120),
                bolsas.save(Bolsa.coletada(doador.getTipoSanguineo(), HOJE.minusDays(120)))));

        assertThat(doacoes.datasDeColetaDoDoador(doador.getId()))
                .containsExactlyInAnyOrder(HOJE, HOJE.minusDays(120));
        assertThat(bolsa.getValidade()).isEqualTo(HOJE.plusDays(35));
        assertThat(doadores.existsByCpf("12345678901")).isTrue();
    }
}
