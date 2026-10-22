package br.cesar.hemocentro.dominio;

import java.util.Optional;

/**
 * T1.8 - Frequencia cardiaca (pulso) na triagem.
 * Fora de 50 a 100 bpm pode indicar problema cardiaco, febre ou nervosismo: o doador nao doa.
 */
public class RegraFrequenciaCardiaca {

    static final int MINIMO_BPM = 50;
    static final int MAXIMO_BPM = 100;

    /** Devolve o motivo de inaptidao, ou vazio se o doador pode doar. */
    public Optional<String> avaliar(int batimentosPorMinuto) {
        if (batimentosPorMinuto < MINIMO_BPM || batimentosPorMinuto > MAXIMO_BPM) {
            return Optional.of("Frequencia cardiaca fora do intervalo de 50 a 100 bpm");
        }
        return Optional.empty();
    }
}
