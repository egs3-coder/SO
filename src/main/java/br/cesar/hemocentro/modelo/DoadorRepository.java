package br.cesar.hemocentro.modelo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DoadorRepository extends JpaRepository<Doador, Long> {

    boolean existsByCpf(String cpf);
}
