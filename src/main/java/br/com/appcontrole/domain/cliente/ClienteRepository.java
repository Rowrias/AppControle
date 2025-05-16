package br.com.appcontrole.domain.cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
    List<Cliente> findAllByOrderByNomeAsc();
    
    Cliente findByNome(String nome);

    Optional<Cliente> findByNomeIgnoreCase(String nome);

    List<Cliente> findByNomeContainingIgnoreCase(String nomeParcial);

    Optional<Cliente> findById(UUID id);

	void deleteById(UUID id);

	boolean existsById(UUID id);
}
