package br.com.appcontrole.domain.entrada;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntradaRepository extends JpaRepository<Entrada, UUID> {
	
    List<Entrada> findByConcluido(boolean status, Sort sort);
    
    // Pendente DataEntrada
    List<Entrada> findByConcluidoFalseOrderByDataEntradaAsc();
    List<Entrada> findByConcluidoFalseOrderByDataEntradaDesc();
    
    // Concluido DataConcluido
    List<Entrada> findByConcluidoTrueOrderByDataConcluidoAsc();
	List<Entrada> findByConcluidoTrueOrderByDataConcluidoDesc();
    
}
