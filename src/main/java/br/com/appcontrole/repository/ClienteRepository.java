package br.com.appcontrole.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.appcontrole.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
