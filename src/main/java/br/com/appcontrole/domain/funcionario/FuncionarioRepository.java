package br.com.appcontrole.domain.funcionario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
	Funcionario findByUsername(String username);
	
    List<Funcionario> findAllByOrderByNomeAsc();
}
