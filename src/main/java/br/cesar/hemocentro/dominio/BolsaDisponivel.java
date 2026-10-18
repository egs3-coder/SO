package br.cesar.hemocentro.dominio;

import java.time.LocalDate;

/** Visão de uma bolsa em estoque, só com o que a regra de seleção precisa. */
public record BolsaDisponivel(Long id, TipoSanguineo tipo, LocalDate validade) {
}
