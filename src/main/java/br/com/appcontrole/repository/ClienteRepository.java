package br.com.appcontrole.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.appcontrole.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	Cliente findByNome(String nome);
	
    List<Cliente> findAllByOrderByNomeAsc();
}
