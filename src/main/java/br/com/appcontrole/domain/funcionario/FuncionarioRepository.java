package br.com.appcontrole.domain.funcionario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>, JpaSpecificationExecutor<Funcionario> {
	
	Funcionario findByNome(String nome);
	
	Funcionario findByUsername(String username);
	
    List<Funcionario> findAllByOrderByNomeAsc();

	boolean existsById(UUID id);

	void deleteById(UUID id);

	Optional<Funcionario> findById(UUID id);
}
