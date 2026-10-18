package br.cesar.hemocentro.dominio;

/** Sexo biológico do doador: define intervalos e limites de doação de sangue total. */
public enum Sexo {
    MASCULINO(60),
    FEMININO(90);

    private final int intervaloMinimoDias;

    Sexo(int intervaloMinimoDias) {
        this.intervaloMinimoDias = intervaloMinimoDias;
    }

    public int intervaloMinimoDias() {
        return intervaloMinimoDias;
    }
}
