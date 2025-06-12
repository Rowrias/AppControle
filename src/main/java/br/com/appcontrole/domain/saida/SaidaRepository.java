package br.com.appcontrole.domain.saida;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaidaRepository extends JpaRepository<Saida, UUID> {

	// Data Saida Descendente
    List<Saida> findAllByOrderByDataSaidaDesc();
    
    // Filtro e paginação cliente
    Page<Saida> findByClienteContainingIgnoreCase(String cliente, Pageable pageable);

    // Filtro e paginação produto
    Page<Saida> findByProdutoContainingIgnoreCase(String produto, Pageable pageable);

    // Método para buscar por cliente E por produto
    Page<Saida> findByClienteContainingIgnoreCaseAndProdutoContainingIgnoreCase(String cliente, String produto, Pageable pageable);
    
    // Metodo para deletar 'saida' com data antiga passada como parametro
    int deleteByDataSaidaBefore(LocalDateTime data); // O 'int' retorna o número de registros deletados

}
