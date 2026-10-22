package br.cesar.hemocentro.dominio;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** T2.5 - CPF valido: 11 digitos e os 2 digitos verificadores corretos. */
class CpfTest {

    @Test
    @DisplayName("CPF com digitos verificadores corretos e valido")
    void cpfValido() {
        assertThat(Cpf.valido("52998224725")).isTrue();
    }

    @Test
    @DisplayName("ultimo digito verificador errado: invalido")
    void digitoErrado() {
        assertThat(Cpf.valido("52998224724")).isFalse();
    }

    @Test
    @DisplayName("todos os digitos iguais: invalido (passa na conta, mas nao existe)")
    void digitosIguais() {
        assertThat(Cpf.valido("11111111111")).isFalse();
    }

    @Test
    @DisplayName("menos de 11 digitos: invalido")
    void tamanhoErrado() {
        assertThat(Cpf.valido("123")).isFalse();
    }

    @Test
    @DisplayName("CPF nulo: invalido")
    void nulo() {
        assertThat(Cpf.valido(null)).isFalse();
    }
}
