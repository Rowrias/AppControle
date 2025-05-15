package br.com.appcontrole.domain.saida;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaidaRepository extends JpaRepository<Saida, UUID> {
	
	// Filtro e paginação
    Page<Saida> findByDestinoContainingIgnoreCase(String destino, Pageable pageable);
	
	// Data Saida Descendente
    List<Saida> findAllByOrderByDataSaidaDesc();
}
