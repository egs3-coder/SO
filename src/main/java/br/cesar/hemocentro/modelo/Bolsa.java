package br.cesar.hemocentro.modelo;

import java.time.LocalDate;

import br.cesar.hemocentro.dominio.BolsaDisponivel;
import br.cesar.hemocentro.dominio.TipoSanguineo;
import br.cesar.hemocentro.dominio.ValidadeBolsa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Bolsa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sanguineo", nullable = false)
    private TipoSanguineo tipoSanguineo;

    @Column(name = "data_coleta", nullable = false)
    private LocalDate dataColeta;

    @Column(nullable = false)
    private LocalDate validade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusBolsa status;

    protected Bolsa() {
    }

    private Bolsa(TipoSanguineo tipoSanguineo, LocalDate dataColeta) {
        this.tipoSanguineo = tipoSanguineo;
        this.dataColeta = dataColeta;
        this.validade = ValidadeBolsa.calcular(dataColeta);
        this.status = StatusBolsa.DISPONIVEL;
    }

    /** Bolsa recém-coletada: entra disponível, com a validade da regra T2.1. */
    public static Bolsa coletada(TipoSanguineo tipoSanguineo, LocalDate dataColeta) {
        return new Bolsa(tipoSanguineo, dataColeta);
    }

    public void marcarUtilizada() {
        this.status = StatusBolsa.UTILIZADA;
    }

    public BolsaDisponivel paraSelecao() {
        return new BolsaDisponivel(id, tipoSanguineo, validade);
    }

    public Long getId() {
        return id;
    }

    public TipoSanguineo getTipoSanguineo() {
        return tipoSanguineo;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public StatusBolsa getStatus() {
        return status;
    }
}
