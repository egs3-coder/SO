package br.cesar.hemocentro.dominio;

import java.time.LocalDate;
import java.util.Optional;

/**
 * T1.6 - Tatuagem ou maquiagem definitiva.
 * Quem fez tatuagem precisa esperar 12 meses para doar (risco de infeccoes como hepatite,
 * que podem nao aparecer nos exames logo depois do contagio).
 */
public class RegraTatuagem {

    static final int MESES_DE_ESPERA = 12;

    /** Devolve o motivo de inaptidao, ou vazio se o doador pode doar. null = nunca fez tatuagem. */
    public Optional<String> avaliar(LocalDate dataUltimaTatuagem, LocalDate hoje) {
        if (dataUltimaTatuagem == null) {
            return Optional.empty();
        }
        LocalDate liberadoEm = dataUltimaTatuagem.plusMonths(MESES_DE_ESPERA);
        if (hoje.isBefore(liberadoEm)) {
            return Optional.of("Tatuagem ou maquiagem definitiva ha menos de 12 meses");
        }
        return Optional.empty();
    }
}
