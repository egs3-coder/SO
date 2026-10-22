package br.cesar.hemocentro.dominio;

import java.util.Optional;

/**
 * T1.7 - Hemoglobina minima para doar.
 * Abaixo do minimo o doador pode ficar anemico depois da doacao.
 */
public class RegraHemoglobina {

    static final double MINIMO_HOMEM = 13.0;
    static final double MINIMO_MULHER = 12.5;

    /** Devolve o motivo de inaptidao, ou vazio se o doador pode doar. */
    public Optional<String> avaliar(Sexo sexo, double hemoglobinaGdl) {
        double minimo = (sexo == Sexo.MASCULINO) ? MINIMO_HOMEM : MINIMO_MULHER;
        if (hemoglobinaGdl < minimo) {
            return Optional.of("Hemoglobina abaixo do minimo de " + minimo + " g/dL");
        }
        return Optional.empty();
    }
}
