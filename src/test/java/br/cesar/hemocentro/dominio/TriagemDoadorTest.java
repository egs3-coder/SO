package br.cesar.hemocentro.dominio;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** H1 — Triagem do doador. Cada classe aninhada corresponde a uma tarefa do story map. */
class TriagemDoadorTest {

    static final LocalDate HOJE = LocalDate.of(2026, 10, 18);

    final TriagemDoador triagem = new TriagemDoador();

    static PerfilDoador perfil(int idade, Sexo sexo, double peso) {
        return new PerfilDoador(HOJE.minusYears(idade), sexo, peso, false);
    }

    static PerfilDoador perfilPadrao() {
        return perfil(30, Sexo.MASCULINO, 70);
    }

    ResultadoTriagem avaliar(PerfilDoador perfil, LocalDate... doacoesAnteriores) {
        return triagem.avaliar(perfil, List.of(doacoesAnteriores), HOJE);
    }

    @Test
    @DisplayName("adulto saudável, sem doações anteriores, está apto")
    void doadorPadraoApto() {
        var resultado = avaliar(perfilPadrao());

        assertThat(resultado.apto()).isTrue();
        assertThat(resultado.motivos()).isEmpty();
    }

    @Nested
    @DisplayName("T1.1 - idade")
    class Idade {

        @Test
        void menorDe16Inapto() {
            var resultado = avaliar(perfil(15, Sexo.FEMININO, 60));

            assertThat(resultado.apto()).isFalse();
            assertThat(resultado.motivos()).containsExactly("Idade mínima para doação é 16 anos");
        }

        @Test
        void dezesseteAnosSemConsentimentoInapto() {
            var resultado = avaliar(perfil(17, Sexo.MASCULINO, 60));

            assertThat(resultado.apto()).isFalse();
            assertThat(resultado.motivos())
                    .containsExactly("Doador menor de 18 anos precisa de consentimento do responsável");
        }

        @Test
        void dezesseteAnosComConsentimentoApto() {
            var comConsentimento = new PerfilDoador(HOJE.minusYears(17), Sexo.MASCULINO, 60, true);

            assertThat(avaliar(comConsentimento).apto()).isTrue();
        }

        @Test
        @DisplayName("faz 16 anos hoje: já pode (com consentimento)")
        void aniversarioDe16Hoje() {
            var perfil = new PerfilDoador(HOJE.minusYears(16), Sexo.FEMININO, 55, true);

            assertThat(avaliar(perfil).apto()).isTrue();
        }

        @Test
        @DisplayName("faz 16 anos amanhã: ainda não pode")
        void aniversarioDe16Amanha() {
            var perfil = new PerfilDoador(HOJE.minusYears(16).plusDays(1), Sexo.FEMININO, 55, true);

            assertThat(avaliar(perfil).apto()).isFalse();
        }

        @Test
        void setentaAnosInapto() {
            var resultado = avaliar(perfil(70, Sexo.MASCULINO, 80), HOJE.minusYears(1));

            assertThat(resultado.apto()).isFalse();
            assertThat(resultado.motivos()).containsExactly("Idade máxima para doação é 69 anos");
        }

        @Test
        void sessentaENoveAnosComHistoricoApto() {
            assertThat(avaliar(perfil(69, Sexo.MASCULINO, 80), HOJE.minusYears(1)).apto()).isTrue();
        }

        @Test
        void primeiraDoacaoAcimaDe60Inapto() {
            var resultado = avaliar(perfil(61, Sexo.FEMININO, 65));

            assertThat(resultado.apto()).isFalse();
            assertThat(resultado.motivos()).containsExactly("Primeira doação só é permitida até 60 anos");
        }

        @Test
        void primeiraDoacaoCom60Apto() {
            assertThat(avaliar(perfil(60, Sexo.FEMININO, 65)).apto()).isTrue();
        }
    }

    @Nested
    @DisplayName("T1.2 - peso")
    class Peso {

        @ParameterizedTest(name = "{0} kg -> inapto")
        @ValueSource(doubles = {30, 49.9})
        void abaixoDe50Inapto(double peso) {
            var resultado = avaliar(perfil(30, Sexo.FEMININO, peso));

            assertThat(resultado.apto()).isFalse();
            assertThat(resultado.motivos()).containsExactly("Peso mínimo para doação é 50 kg");
        }

        @Test
        void exatamente50Apto() {
            assertThat(avaliar(perfil(30, Sexo.FEMININO, 50)).apto()).isTrue();
        }

        @Test
        @DisplayName("acumula motivos: idade e peso ao mesmo tempo")
        void acumulaMotivos() {
            var resultado = avaliar(perfil(15, Sexo.FEMININO, 45));

            assertThat(resultado.motivos()).containsExactlyInAnyOrder(
                    "Idade mínima para doação é 16 anos",
                    "Peso mínimo para doação é 50 kg");
        }
    }

    @Nested
    @DisplayName("T1.3 - intervalo desde a última doação")
    class Intervalo {

        @Test
        void homemCom59DiasInapto() {
            var resultado = avaliar(perfil(30, Sexo.MASCULINO, 70), HOJE.minusDays(59));

            assertThat(resultado.apto()).isFalse();
            assertThat(resultado.motivos()).containsExactly("Intervalo mínimo entre doações é de 60 dias");
        }

        @Test
        void homemCom60DiasApto() {
            assertThat(avaliar(perfil(30, Sexo.MASCULINO, 70), HOJE.minusDays(60)).apto()).isTrue();
        }

        @Test
        void mulherCom89DiasInapta() {
            var resultado = avaliar(perfil(30, Sexo.FEMININO, 60), HOJE.minusDays(89));

            assertThat(resultado.apto()).isFalse();
            assertThat(resultado.motivos()).containsExactly("Intervalo mínimo entre doações é de 90 dias");
        }

        @Test
        void mulherCom90DiasApta() {
            assertThat(avaliar(perfil(30, Sexo.FEMININO, 60), HOJE.minusDays(90)).apto()).isTrue();
        }

        @Test
        @DisplayName("considera a doação mais recente, mesmo com histórico fora de ordem")
        void usaDoacaoMaisRecente() {
            var resultado = avaliar(perfil(30, Sexo.MASCULINO, 70),
                    HOJE.minusDays(300), HOJE.minusDays(10), HOJE.minusDays(200));

            assertThat(resultado.apto()).isFalse();
        }
    }

    @Nested
    @DisplayName("T1.4 - limite de doações em 12 meses")
    class LimiteAnual {

        @Test
        void homemCom4DoacoesNoAnoInapto() {
            var resultado = avaliar(perfil(30, Sexo.MASCULINO, 70),
                    HOJE.minusDays(61), HOJE.minusDays(122), HOJE.minusDays(183), HOJE.minusDays(244));

            assertThat(resultado.apto()).isFalse();
            assertThat(resultado.motivos()).containsExactly("Limite de 4 doações em 12 meses atingido");
        }

        @Test
        void homemCom3DoacoesNoAnoApto() {
            assertThat(avaliar(perfil(30, Sexo.MASCULINO, 70),
                    HOJE.minusDays(61), HOJE.minusDays(122), HOJE.minusDays(183)).apto()).isTrue();
        }

        @Test
        void mulherCom3DoacoesNoAnoInapta() {
            var resultado = avaliar(perfil(30, Sexo.FEMININO, 60),
                    HOJE.minusDays(91), HOJE.minusDays(182), HOJE.minusDays(273));

            assertThat(resultado.apto()).isFalse();
            assertThat(resultado.motivos()).containsExactly("Limite de 3 doações em 12 meses atingido");
        }

        @Test
        @DisplayName("doação de mais de 12 meses atrás não conta para o limite")
        void doacaoAntigaNaoConta() {
            assertThat(avaliar(perfil(30, Sexo.FEMININO, 60),
                    HOJE.minusDays(91), HOJE.minusDays(182), HOJE.minusDays(366)).apto()).isTrue();
        }
    }
}
