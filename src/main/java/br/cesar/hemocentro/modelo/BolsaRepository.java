package br.cesar.hemocentro.modelo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface BolsaRepository extends JpaRepository<Bolsa, Long> {

    /** Estoque utilizável (consulta). */
    List<Bolsa> findByStatusAndValidadeGreaterThanEqual(StatusBolsa status, LocalDate hoje);

    /**
     * Mesmo filtro, mas trava as linhas (SELECT ... FOR UPDATE) até o fim da transação:
     * duas transfusões simultâneas não podem receber a mesma bolsa.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Bolsa b where b.status = :status and b.validade >= :hoje")
    List<Bolsa> travarDisponiveis(@Param("status") StatusBolsa status, @Param("hoje") LocalDate hoje);
}
