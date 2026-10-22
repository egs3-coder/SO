package br.cesar.hemocentro.dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * T1.9 - Pressao arterial na triagem.
 * Pressao muito alta aumenta o risco de mal-estar durante a doacao.
 * Devolve todos os motivos de uma vez, como o TriagemDoador.
 */
public class RegraPressaoArterial {

    static final int SISTOLICA_MAXIMA = 180;
    static final int DIASTOLICA_MAXIMA = 100;

    /** Lista vazia = pressao dentro do permitido. */
    public List<String> avaliar(int sistolica, int diastolica) {
        List<String> motivos = new ArrayList<>();
        if (sistolica > SISTOLICA_MAXIMA) {
            motivos.add("Pressao sistolica acima de 180 mmHg");
        }
        if (diastolica > DIASTOLICA_MAXIMA) {
            motivos.add("Pressao diastolica acima de 100 mmHg");
        }
        return motivos;
    }
}
