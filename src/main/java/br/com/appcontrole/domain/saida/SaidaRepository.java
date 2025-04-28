package br.com.appcontrole.domain.saida;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaidaRepository extends JpaRepository<Saida, Long> {
	// Método listar DATA em ordem decrescente
    @Query("SELECT s FROM Saida s WHERE s.dataSaida IS NOT NULL ORDER BY s.dataSaida DESC")
    List<Saida> findByOrderByDataSaidaDesc();
}
