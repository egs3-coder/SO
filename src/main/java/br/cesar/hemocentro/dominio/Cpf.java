package br.cesar.hemocentro.dominio;

public final class Cpf {

    private Cpf() {
    }

    /** Por enquanto so confere o formato (11 digitos), como a API ja faz hoje: o teste deve ficar vermelho. */
    public static boolean valido(String cpf) {
        return cpf != null && cpf.matches("\\d{11}");
    }
}
