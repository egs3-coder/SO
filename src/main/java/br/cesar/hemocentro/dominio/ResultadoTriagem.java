package br.cesar.hemocentro.dominio;

import java.util.List;

/** Resultado da triagem: apto ou não, e todos os motivos de inaptidão encontrados. */
public record ResultadoTriagem(boolean apto, List<String> motivos) {

    public ResultadoTriagem {
        motivos = List.copyOf(motivos);
    }

    public static ResultadoTriagem de(List<String> motivos) {
        return new ResultadoTriagem(motivos.isEmpty(), motivos);
    }
}
