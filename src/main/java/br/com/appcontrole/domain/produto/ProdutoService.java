package br.com.appcontrole.domain.produto;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.appcontrole.domain.CRUD;

@Service
public class ProdutoService implements CRUD<Produto, UUID> {
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@Override
	public Produto insere(Produto produto) {
		return produtoRepository.save(produto);
	}

	@Override
	public Produto atualiza(Produto produto) {
		if (produto.getId() == null || !produtoRepository.existsById(produto.getId())) {
            throw new IllegalArgumentException("Não encontrado");
        }
        // Persistir as alterações
        return produtoRepository.save(produto);
	}

	@Override
	public void remove(UUID id) {
		produtoRepository.deleteById(id);
	}

	@Override
	public List<Produto> buscaTodos() {
		return produtoRepository.findAll();
	}

	@Override
	public Produto buscaPorId(UUID id) {
		return produtoRepository.findById(id).orElse(null);
	}
	
	// ----------------
	
	// public List<Produto> buscaTodosOrdenado(Sort sort) {
    //    return produtoRepository.findAll(sort);
    // }

	// Método para listar produtos com paginação e ordenação
	public Page<Produto> listarPaginado(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }
	
	public Produto buscaPorNome(String nome) {
		return produtoRepository.findByNome(nome);
	}

	public Produto findByNomeIgnoreCase(String nomeProduto) {
		return produtoRepository.findByNomeIgnoreCase(nomeProduto)
		        .orElseGet(() -> {
		            Produto novo = new Produto();
		            novo.setNome(nomeProduto);
		            return produtoRepository.save(novo);
		        });
	}

	// Para o autocomplete
	public List<Produto> findByNomeContainingIgnoreCase(String nomeParcial) {
        return produtoRepository.findByNomeContainingIgnoreCase(nomeParcial);
    }
	
	// Método para buscar produtos por nome com paginação e ordenação
	public Page<Produto> buscarPorNomePaginado(String nomeParcial, Pageable pageable) {
        return produtoRepository.findByNomeContainingIgnoreCase(nomeParcial, pageable);
    }
	
}
