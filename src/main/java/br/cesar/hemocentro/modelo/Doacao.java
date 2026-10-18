package br.cesar.hemocentro.modelo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doador_id", nullable = false)
    private Doador doador;

    @Column(name = "data_coleta", nullable = false)
    private LocalDate dataColeta;

    @OneToOne(optional = false)
    @JoinColumn(name = "bolsa_id", nullable = false, unique = true)
    private Bolsa bolsa;

    protected Doacao() {
    }

    public Doacao(Doador doador, LocalDate dataColeta, Bolsa bolsa) {
        this.doador = doador;
        this.dataColeta = dataColeta;
        this.bolsa = bolsa;
    }

    public Long getId() {
        return id;
    }

    public Doador getDoador() {
        return doador;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public Bolsa getBolsa() {
        return bolsa;
    }
}
