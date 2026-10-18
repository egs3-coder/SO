package br.cesar.hemocentro.dominio;

/** Sexo biológico do doador: define intervalos e limites de doação de sangue total. */
public enum Sexo {
    MASCULINO(60, 4),
    FEMININO(90, 3);

    private final int intervaloMinimoDias;
    private final int maximoDoacoesEm12Meses;

    Sexo(int intervaloMinimoDias, int maximoDoacoesEm12Meses) {
        this.intervaloMinimoDias = intervaloMinimoDias;
        this.maximoDoacoesEm12Meses = maximoDoacoesEm12Meses;
    }

    public int intervaloMinimoDias() {
        return intervaloMinimoDias;
    }

    public int maximoDoacoesEm12Meses() {
        return maximoDoacoesEm12Meses;
    }
}
