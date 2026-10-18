package br.cesar.hemocentro.api;

import java.time.LocalDate;
import java.util.List;

import br.cesar.hemocentro.dominio.Sexo;
import br.cesar.hemocentro.dominio.TipoSanguineo;
import br.cesar.hemocentro.modelo.Bolsa;
import br.cesar.hemocentro.modelo.Doacao;
import br.cesar.hemocentro.modelo.Doador;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/** Contratos JSON da API. Tipos sanguíneos trafegam como símbolo ("O-", "AB+"). */
public final class Dtos {

    private Dtos() {
    }

    public record NovoDoadorRequest(
            @NotBlank String nome,
            @NotBlank @Pattern(regexp = "\\d{11}", message = "deve conter 11 dígitos") String cpf,
            @NotNull @Past LocalDate dataNascimento,
            @NotNull Sexo sexo,
            @NotNull @Positive Double pesoKg,
            @NotBlank String tipoSanguineo,
            boolean consentimentoResponsavel) {

        Doador paraEntidade() {
            return new Doador(nome, cpf, dataNascimento, sexo, pesoKg,
                    TipoSanguineo.deSimbolo(tipoSanguineo), consentimentoResponsavel);
        }
    }

    public record DoadorResponse(Long id, String nome, String cpf, LocalDate dataNascimento, Sexo sexo,
                                 double pesoKg, String tipoSanguineo, boolean consentimentoResponsavel) {

        static DoadorResponse de(Doador d) {
            return new DoadorResponse(d.getId(), d.getNome(), d.getCpf(), d.getDataNascimento(), d.getSexo(),
                    d.getPesoKg(), d.getTipoSanguineo().simbolo(), d.isConsentimentoResponsavel());
        }
    }

    public record DoacaoResponse(Long doacaoId, Long bolsaId, String tipoSanguineo,
                                 LocalDate dataColeta, LocalDate validade) {

        static DoacaoResponse de(Doacao d) {
            return new DoacaoResponse(d.getId(), d.getBolsa().getId(), d.getBolsa().getTipoSanguineo().simbolo(),
                    d.getDataColeta(), d.getBolsa().getValidade());
        }
    }

    public record TransfusaoRequest(@NotBlank String tipoReceptor, @Min(1) @Max(20) int quantidade) {
    }

    public record BolsaResponse(Long id, String tipoSanguineo, LocalDate validade) {

        static BolsaResponse de(Bolsa b) {
            return new BolsaResponse(b.getId(), b.getTipoSanguineo().simbolo(), b.getValidade());
        }
    }

    public record TransfusaoResponse(String tipoReceptor, List<BolsaResponse> bolsas) {
    }
}
