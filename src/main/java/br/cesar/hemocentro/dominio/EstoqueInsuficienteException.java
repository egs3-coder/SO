package br.cesar.hemocentro.dominio;

public class EstoqueInsuficienteException extends RuntimeException {

    private final int solicitadas;
    private final int disponiveis;

    public EstoqueInsuficienteException(TipoSanguineo receptor, int solicitadas, int disponiveis) {
        super("Estoque insuficiente para receptor " + receptor.simbolo()
                + ": solicitadas " + solicitadas + ", compatíveis disponíveis " + disponiveis);
        this.solicitadas = solicitadas;
        this.disponiveis = disponiveis;
    }

    public int getSolicitadas() {
        return solicitadas;
    }

    public int getDisponiveis() {
        return disponiveis;
    }
}
