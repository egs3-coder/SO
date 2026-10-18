package br.cesar.hemocentro.dominio;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * Triagem do doador, baseada (de forma simplificada) na Portaria de Consolidação MS nº 5/2017, Anexo IV.
 * Avalia todas as regras e devolve todos os motivos, para o atendente explicar de uma vez ao doador.
 */
public class TriagemDoador {

    static final int IDADE_MINIMA = 16;
    static final int MAIORIDADE = 18;
    static final int IDADE_MAXIMA = 69;
    static final int IDADE_MAXIMA_PRIMEIRA_DOACAO = 60;

    public ResultadoTriagem avaliar(PerfilDoador perfil, List<LocalDate> doacoesAnteriores, LocalDate hoje) {
        List<String> motivos = new ArrayList<>();
        avaliarIdade(perfil, doacoesAnteriores, hoje, motivos);
        return ResultadoTriagem.de(motivos);
    }

    private void avaliarIdade(PerfilDoador perfil, List<LocalDate> doacoesAnteriores, LocalDate hoje,
                              List<String> motivos) {
        int idade = Period.between(perfil.dataNascimento(), hoje).getYears();

        if (idade < IDADE_MINIMA) {
            motivos.add("Idade mínima para doação é 16 anos");
        } else if (idade < MAIORIDADE && !perfil.consentimentoResponsavel()) {
            motivos.add("Doador menor de 18 anos precisa de consentimento do responsável");
        } else if (idade > IDADE_MAXIMA) {
            motivos.add("Idade máxima para doação é 69 anos");
        } else if (idade > IDADE_MAXIMA_PRIMEIRA_DOACAO && doacoesAnteriores.isEmpty()) {
            motivos.add("Primeira doação só é permitida até 60 anos");
        }
    }
}
