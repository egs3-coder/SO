package br.cesar.hemocentro.dominio;

import java.time.LocalDate;

/** Dados do doador que a triagem precisa avaliar. */
public record PerfilDoador(LocalDate dataNascimento, Sexo sexo, double pesoKg, boolean consentimentoResponsavel) {
}
