package br.cesar.hemocentro.aplicacao;

import java.util.List;

public class DoadorInaptoException extends RuntimeException {

    private final List<String> motivos;

    public DoadorInaptoException(List<String> motivos) {
        super("Doador inapto para doação");
        this.motivos = List.copyOf(motivos);
    }

    public List<String> getMotivos() {
        return motivos;
    }
}
