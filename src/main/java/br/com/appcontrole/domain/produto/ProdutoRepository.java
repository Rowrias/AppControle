package br.com.appcontrole.domain.produto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	List<Produto> findAllByOrderByNomeAsc();

	Produto findByNome(String nome);
	
	Optional<Produto> findByNomeIgnoreCase(String nome);

}
