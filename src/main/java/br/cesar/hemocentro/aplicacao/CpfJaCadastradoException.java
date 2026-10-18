package br.cesar.hemocentro.aplicacao;

public class CpfJaCadastradoException extends RuntimeException {

    public CpfJaCadastradoException(String cpf) {
        super("Já existe doador cadastrado com o CPF " + cpf);
    }
}
