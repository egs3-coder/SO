package br.cesar.hemocentro.dominio;

/**
 * T2.5 - Validacao de CPF pelos digitos verificadores.
 * Os 2 ultimos digitos sao calculados a partir dos 9 primeiros; se a conta nao bate, o CPF foi digitado errado.
 */
public final class Cpf {

    private Cpf() {
    }

    public static boolean valido(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }
        // 111.111.111-11, 222.222.222-22... passam na conta, mas nao sao CPFs reais
        if (cpf.chars().distinct().count() == 1) {
            return false;
        }
        int primeiro = digitoVerificador(cpf, 9);
        int segundo = digitoVerificador(cpf, 10);
        return primeiro == digito(cpf, 9) && segundo == digito(cpf, 10);
    }

    /** Soma cada digito multiplicado por um peso decrescente; resto da divisao por 11 define o digito. */
    private static int digitoVerificador(String cpf, int quantidade) {
        int soma = 0;
        int peso = quantidade + 1;
        for (int i = 0; i < quantidade; i++) {
            soma += digito(cpf, i) * peso;
            peso--;
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

    private static int digito(String cpf, int posicao) {
        return cpf.charAt(posicao) - '0';
    }
}
