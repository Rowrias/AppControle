package br.com.appcontrole.domain.produto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.appcontrole.domain.CRUD;

@Service
public class ProdutoService implements CRUD<Produto, Long> {
	
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
	public void remove(Long id) {
		produtoRepository.deleteById(id);
	}

	@Override
	public List<Produto> buscaTodos() {
		return produtoRepository.findAll();
	}

	@Override
	public Produto buscaPorId(Long id) {
		return produtoRepository.findById(id).orElse(null);
	}

	public List<Produto> buscaTodosOrdenadoPorNome() {
		return produtoRepository.findAllByOrderByNomeAsc();
	}

	public Produto buscaPorNome(String nome) {
		return produtoRepository.findByNome(nome);
	}
	
}
