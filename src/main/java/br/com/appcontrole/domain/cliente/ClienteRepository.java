package br.com.appcontrole.domain.cliente;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
    List<Cliente> findAllByOrderByNomeAsc();
    
    Cliente findByNome(String nome);
}
