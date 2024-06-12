package br.com.appcontrole.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.appcontrole.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
	Funcionario findByUsername(String username);
	
    List<Funcionario> findAllByOrderByNomeAsc();
}
