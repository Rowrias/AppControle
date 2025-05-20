package br.com.appcontrole.domain.produto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

	// List<Produto> findAllByOrderByNomeAsc();

    Produto findByNome(String nome);

    Optional<Produto> findByNomeIgnoreCase(String nome);

    List<Produto> findByNomeContainingIgnoreCase(String nomeParcial);

    Page<Produto> findByNomeContainingIgnoreCase(String nomeParcial, Pageable pageable);

    Optional<Produto> findById(UUID id);

    void deleteById(UUID id);

    boolean existsById(UUID id);

}
