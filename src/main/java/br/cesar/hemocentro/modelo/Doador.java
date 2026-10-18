package br.cesar.hemocentro.modelo;

import java.time.LocalDate;

import br.cesar.hemocentro.dominio.PerfilDoador;
import br.cesar.hemocentro.dominio.Sexo;
import br.cesar.hemocentro.dominio.TipoSanguineo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Doador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sexo sexo;

    @Column(name = "peso_kg", nullable = false)
    private double pesoKg;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sanguineo", nullable = false)
    private TipoSanguineo tipoSanguineo;

    @Column(name = "consentimento_responsavel", nullable = false)
    private boolean consentimentoResponsavel;

    protected Doador() {
    }

    public Doador(String nome, String cpf, LocalDate dataNascimento, Sexo sexo, double pesoKg,
                  TipoSanguineo tipoSanguineo, boolean consentimentoResponsavel) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.pesoKg = pesoKg;
        this.tipoSanguineo = tipoSanguineo;
        this.consentimentoResponsavel = consentimentoResponsavel;
    }

    /** O que a regra de triagem precisa saber sobre este doador. */
    public PerfilDoador perfil() {
        return new PerfilDoador(dataNascimento, sexo, pesoKg, consentimentoResponsavel);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public TipoSanguineo getTipoSanguineo() {
        return tipoSanguineo;
    }

    public boolean isConsentimentoResponsavel() {
        return consentimentoResponsavel;
    }
}
