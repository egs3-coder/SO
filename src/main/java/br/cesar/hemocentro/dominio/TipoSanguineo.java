package br.cesar.hemocentro.dominio;

import java.util.EnumSet;
import java.util.Set;

/**
 * Tipos sanguíneos ABO/Rh.
 *
 * <p>Regra de compatibilidade de hemácias: o doador só pode ter antígenos que o receptor
 * também tenha (A, B e D/Rh). Se o doador levar um antígeno que o receptor não tem, o
 * receptor tem anticorpos contra ele e ocorre reação hemolítica.
 */
public enum TipoSanguineo {

    O_NEGATIVO("O-"),
    O_POSITIVO("O+", Antigeno.D),
    A_NEGATIVO("A-", Antigeno.A),
    A_POSITIVO("A+", Antigeno.A, Antigeno.D),
    B_NEGATIVO("B-", Antigeno.B),
    B_POSITIVO("B+", Antigeno.B, Antigeno.D),
    AB_NEGATIVO("AB-", Antigeno.A, Antigeno.B),
    AB_POSITIVO("AB+", Antigeno.A, Antigeno.B, Antigeno.D);

    private enum Antigeno { A, B, D }

    private final String simbolo;
    private final Set<Antigeno> antigenos;

    TipoSanguineo(String simbolo, Antigeno... antigenos) {
        this.simbolo = simbolo;
        this.antigenos = antigenos.length == 0 ? EnumSet.noneOf(Antigeno.class) : EnumSet.of(antigenos[0], antigenos);
    }

    /** Hemácias deste tipo podem ser transfundidas em um receptor do tipo informado? */
    public boolean podeDoarPara(TipoSanguineo receptor) {
        return receptor.antigenos.containsAll(this.antigenos);
    }

    public String simbolo() {
        return simbolo;
    }

    public static TipoSanguineo deSimbolo(String simbolo) {
        for (TipoSanguineo tipo : values()) {
            if (tipo.simbolo.equalsIgnoreCase(simbolo.trim())) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo sanguíneo inválido: " + simbolo);
    }
}
