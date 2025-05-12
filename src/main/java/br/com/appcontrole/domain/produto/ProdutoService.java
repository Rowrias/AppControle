package br.com.appcontrole.domain.produto;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.appcontrole.domain.CRUD;
import br.com.appcontrole.domain.entrada.Entrada;

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
	
	//
	public List<Produto> buscaTodosOrdenadoPorNome() {
		return produtoRepository.findAllByOrderByNomeAsc();
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

	public List<Entrada> buscaPorNomeParcial(String nomeParcial) {
		return produtoRepository.findByNomeContainingIgnoreCase(nomeParcial);
	}
	
}
