package br.cesar.hemocentro.modelo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    /** Histórico usado pela triagem (intervalo e limite anual). */
    @Query("select d.dataColeta from Doacao d where d.doador.id = :doadorId")
    List<LocalDate> datasDeColetaDoDoador(@Param("doadorId") Long doadorId);
}
