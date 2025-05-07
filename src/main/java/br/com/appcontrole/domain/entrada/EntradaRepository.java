package br.com.appcontrole.domain.entrada;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {
	
    List<Entrada> findByConcluido(boolean status);

    // Método para encontrar todas as entradas PENDENTES ordenadas pelo ID em ordem decrescente
    @Query("SELECT e FROM Entrada e WHERE e.concluido = false ORDER BY e.id DESC")
    List<Entrada> findPendentesOrderByIdDesc();

    // Método para encontrar todas as entradas CONCLUIDAS ordenadas pelo ID em ordem decrescente
    @Query("SELECT e FROM Entrada e WHERE e.concluido = true ORDER BY e.id DESC")
    List<Entrada> findConcluidasOrderByIdDesc();

    // Método para encontrar todas as entradas PENDENTES ordenadas pela DATA em ordem decrescente
    List<Entrada> findByConcluidoFalseOrderByDataEntradaDesc();
    
	// Método para encontrar todas as entradas CONCLUIDAS ordenadas pelo DATA em ordem decrescente
	List<Entrada> findByConcluidoTrueOrderByDataConcluidoDesc();
}
