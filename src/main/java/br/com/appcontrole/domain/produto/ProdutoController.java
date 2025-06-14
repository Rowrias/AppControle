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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;

	// Insere
	@PostMapping("/lista")
    public String novoProduto(@Valid @ModelAttribute("produto") Produto produto, 
					    		BindingResult result,
					    		RedirectAttributes attr) {
		
		// Verifica erros de validação primeiro
		if (result.hasErrors()) {
			attr.addFlashAttribute("org.springframework.validation.BindingResult.produto", result);
			attr.addFlashAttribute("produto", produto); // Para manter os dados preenchidos no formulário
            attr.addFlashAttribute("erro", "Verifique os dados do produto e tente novamente."); // Mensagem genérica
            return "redirect:/produtos/lista"; // Redireciona de volta para a página de lista/cadastro
		
		}
		
		// Verifica se o produto já existe pelo nome
		Produto produtoExistente = produtoService.buscaPorNome(produto.getNome());
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
                                @RequestParam(name = "page", defaultValue = "0") int page,       		// Parâmetro de página
                                @RequestParam(name = "size", defaultValue = "20") int size,       		// Parâmetro de tamanho da página
                                @RequestParam(name = "buscaNome", required = false) String buscaNome, 	// Parâmetro de busca por nome
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
        
        // Se o 'produto' já não estiver no modelo (vindo de um flash attribute após erro de validação),
        // adicione um novo objeto Produto vazio para o formulário de adição.
        if (!model.containsAttribute("produto")) {
        	model.addAttribute("produto", new Produto());
        }
        return "produtos/lista";
        
    }
	
	// Edita
	@GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable UUID id, Model model) {
		// Se o 'produto' já não estiver no modelo (vindo de um flash attribute após erro de validação),
        // busque o produto pelo ID.
		if (!model.containsAttribute("produto")) {
			Produto produto = produtoService.buscaPorId(id);			
            model.addAttribute("produto", produto);
        }
        return "produtos/editar";
    }

	@PostMapping("/editar/{id}")
    public String atualizarProduto(@PathVariable UUID id, 
						    		@Valid Produto produto,
						    		BindingResult result,
						    		RedirectAttributes attr) {
		
		// Define o ID do produto no objeto 'produto' para que a validação e atualização funcionem corretamente
		produto.setId(id);

		// Validação
		if (result.hasErrors()) {
			attr.addFlashAttribute("org.springframework.validation.BindingResult.produto", result);
			attr.addFlashAttribute("produto", produto); // Para manter os dados preenchidos no formulário
            attr.addFlashAttribute("erro", "Verifique os dados do produto e tente novamente."); // Mensagem genérica           
            return "redirect:/produtos/editar/" + id; // Redireciona de volta para a página de edição
        }
		
		
		// Verifica se existe outro produto com o mesmo nome
		Produto produtoExistente = produtoService.buscaPorNome(produto.getNome());
        if (produtoExistente != null && !produtoExistente.getId().equals(id)) {
            attr.addFlashAttribute("erro", "Já existe um produto com esse nome.");
            attr.addFlashAttribute("produto", produto); // Preserve os dados

            return "redirect:/produtos/editar/" + id; // Volta para a edição se houver conflito de nome
        }
        
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
        	attr.addFlashAttribute("erro", "Não é possível excluir o produto devido a registros dependentes na tabela 'Entrada'.");
        }
        return "redirect:/produtos/lista";
    }
}
