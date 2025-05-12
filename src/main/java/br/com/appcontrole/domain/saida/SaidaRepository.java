package br.com.appcontrole.domain.saida;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaidaRepository extends JpaRepository<Saida, UUID> {
	
	// Novo método com filtro e paginação por destino
    Page<Saida> findByDestinoContainingIgnoreCase(String destino, Pageable pageable);
	
	// Método listar DATA em ordem decrescente
    @Query("SELECT s FROM Saida s WHERE s.dataSaida IS NOT NULL ORDER BY s.dataSaida DESC")
    List<Saida> findAllByOrderByDataSaidaDesc();
}
