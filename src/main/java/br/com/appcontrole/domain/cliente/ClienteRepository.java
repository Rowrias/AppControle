package br.com.appcontrole.domain.cliente;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.appcontrole.domain.entrada.Entrada;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
    List<Cliente> findAllByOrderByNomeAsc();
    
    Cliente findByNome(String nome);
    
    Optional<Cliente> findByNomeIgnoreCase(String nome);

	Collection<Entrada> findByNomeContainingIgnoreCase(String nomeParcial);
}
