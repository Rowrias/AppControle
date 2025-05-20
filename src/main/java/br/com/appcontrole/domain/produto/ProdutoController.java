package br.com.appcontrole.domain.produto;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;

	// Insere
	@PostMapping("/lista")
    public String novoProduto(Produto produto, RedirectAttributes attr) {
		
		Produto produtoExistente = produtoService.buscaPorNome(produto.getNome());
        // Verifica se o produto já existe pelo nome
        if (produtoExistente != null) {
            attr.addFlashAttribute("erro", "Produto já existe.");

            return "redirect:/produtos/lista";
        } else {
            // Produto não existe, então insere o novo cliente
        	produtoService.insere(produto);
            attr.addFlashAttribute("mensagem", "Produto adicionado com sucesso.");
        }
        return "redirect:/produtos/lista";
    }
	
	@GetMapping("/lista")
    public String listaProdutos(Model model,
                                @RequestParam(name = "page", defaultValue = "0") int page,       // Parâmetro de página
                                @RequestParam(name = "size", defaultValue = "3") int size,       // Parâmetro de tamanho da página
                                @RequestParam(name = "buscaNome", required = false) String buscaNome, // NOVO: Parâmetro de busca por nome
                                @RequestParam(name = "sortBy", defaultValue = "nome") String sortBy,
                                @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Produto> paginaProdutos;

        // Lógica de busca e paginação
        if (buscaNome != null && !buscaNome.isBlank()) {
            paginaProdutos = produtoService.buscarPorNomePaginado(buscaNome, pageable);
        } else {
            paginaProdutos = produtoService.listarPaginado(pageable);
        }
        
        model.addAttribute("paginaProdutos", paginaProdutos); // Mude de "produtos" para "paginaProdutos"
        model.addAttribute("buscaNome", buscaNome); // Adiciona o termo de busca de volta ao modelo
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        
        return "produtos/lista";
    }
	
	// Edita
	@GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable UUID id, Model model) {
		Produto produto = produtoService.buscaPorId(id);
        model.addAttribute("produto", produto);
        return "produtos/editar";
    }

	@PostMapping("/editar/{id}")
    public String atualizarProduto(@PathVariable UUID id, Produto produto, RedirectAttributes attr) {
        // Verifica se existe outro produto com o mesmo nome
		Produto produtoExistente = produtoService.buscaPorNome(produto.getNome());
        if (produtoExistente != null && !produtoExistente.getId().equals(id)) {
            attr.addFlashAttribute("erro", "Já existe um produto com esse nome.");
            return "redirect:/produtos";
        }
        
        produto.setId(id);
        produtoService.atualiza(produto);
        attr.addFlashAttribute("mensagem", "Produto atualizado com sucesso.");
        return "redirect:/produtos/lista";
    }
	
	// Remove
	@GetMapping("/remover/{id}")
    public String removerProduto(@PathVariable UUID id, RedirectAttributes attr) {
    	try {
    		produtoService.remove(id);
            attr.addFlashAttribute("mensagem", "Produto removido com sucesso.");
        } catch (DataIntegrityViolationException e) {
        	attr.addFlashAttribute("erro", "Não é possível excluir o produto devido a registros dependentes na tabela 'saida'.");
        }
        return "redirect:/produtos/lista";
    }
}
