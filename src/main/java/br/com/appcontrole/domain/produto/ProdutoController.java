package br.com.appcontrole.domain.produto;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	// Lista
	@GetMapping("/lista")
	public String listaProdutos(Model model) {
		List<Produto> produto = produtoService.buscaTodosOrdenadoPorNome();
		model.addAttribute("produtos", produto);
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
