package br.com.appcontrole.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.appcontrole.model.Entrada;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {
    List<Entrada> findByConcluido(boolean status);

    // Método para encontrar todas as entradas pendentes ordenadas pelo ID em ordem decrescente
    @Query("SELECT e FROM Entrada e WHERE e.concluido = false ORDER BY e.id DESC")
    List<Entrada> findPendentesOrderByIdDesc();

    // Método para encontrar todas as entradas concluídas ordenadas pelo ID em ordem decrescente
    @Query("SELECT e FROM Entrada e WHERE e.concluido = true ORDER BY e.id DESC")
    List<Entrada> findConcluidasOrderByIdDesc();
}
