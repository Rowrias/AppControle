package br.com.appcontrole.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.appcontrole.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

}
