package br.com.appcontrole.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.appcontrole.model.Entrada;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {

}
