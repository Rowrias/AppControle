package br.com.appcontrole.domain.cliente;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;

	// Insere
	@PostMapping("/lista")
    public String novoCliente(@Valid @ModelAttribute("cliente") Cliente cliente, 
    							BindingResult result,
    							RedirectAttributes attr) {
		
		// Verifica erros de validação primeiro
        if (result.hasErrors()) {
            // Adiciona o BindingResult e o objeto 'cliente' como flash attributes
            attr.addFlashAttribute("org.springframework.validation.BindingResult.cliente", result);
            attr.addFlashAttribute("cliente", cliente); // Para preservar os dados no formulário
            attr.addFlashAttribute("erro", "Verifique os dados do cliente e tente novamente."); // Mensagem genérica
            return "redirect:/clientes/lista"; // Redireciona de volta para o formulário
        }
		
        // Lógica para verificar nome duplicado
        Cliente clienteExistente = clienteService.buscaPorNome(cliente.getNome());
        // Verifica se o cliente já existe pelo nome
        if (clienteExistente != null) {
            attr.addFlashAttribute("erro", "Cliente já existe.");
            return "redirect:/clientes/lista";
        } else {
            // Cliente não existe, então insere o novo cliente
            clienteService.insere(cliente);
            attr.addFlashAttribute("mensagem", "Cliente adicionado com sucesso.");
        }
        return "redirect:/clientes/lista";
    }
	
	// Lista
	@GetMapping("/lista")
    public String listaClientes(Model model,
                                 @RequestParam(name = "sortBy", required = false, defaultValue = "nome") String sortBy,
                                 @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDirection) {

        Sort sort = Sort.by(sortBy);
        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }

        List<Cliente> clientes = clienteService.buscaTodosOrdenado(sort);
        model.addAttribute("clientes", clientes);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        
        // Se o cliente já não estiver no modelo (vindo de um flash attribute após erro de validação),
        // adicione um novo objeto Cliente para o formulário de adição.
        if (!model.containsAttribute("cliente")) {
            model.addAttribute("cliente", new Cliente());
        }
        return "clientes/lista";
    }
	
	// Edita
	@GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable UUID id, Model model) {
		if (!model.containsAttribute("cliente")) {
			Cliente cliente = clienteService.buscaPorId(id);
	        model.addAttribute("cliente", cliente);
		}
        return "clientes/editar";
    }

	@PostMapping("/editar/{id}")
    public String atualizarCliente(@PathVariable UUID id, 
    								@Valid @ModelAttribute("cliente") Cliente cliente,
    								BindingResult result,
						    		RedirectAttributes attr) {
		
		// Verifica erros de validação primeiro
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.cliente", result);
            attr.addFlashAttribute("cliente", cliente); // Para preservar os dados no formulário
            attr.addFlashAttribute("erro", "Verifique os dados do cliente e tente novamente.");
            return "redirect:/clientes/editar/" + id; // Redireciona de volta para o formulário de edição
        }
		
        // Verifica se existe outro cliente com o mesmo nome, exceto o cliente atual
        Cliente clienteExistente = clienteService.buscaPorNome(cliente.getNome());
        if (clienteExistente != null && !clienteExistente.getId().equals(id)) {
            attr.addFlashAttribute("erro", "Já existe um cliente com esse nome.");
            attr.addFlashAttribute("cliente", cliente); // Preserve os dados
            return "redirect:/clientes";
        }
        
        cliente.setId(id);
        clienteService.atualiza(cliente);
        attr.addFlashAttribute("mensagem", "Cliente atualizado com sucesso.");
        return "redirect:/clientes/lista";
    }
    
	// Remove
    @GetMapping("/remover/{id}")
    public String removerCliente(@PathVariable UUID id, RedirectAttributes attr) {
    	try {
            clienteService.remove(id);
            attr.addFlashAttribute("mensagem", "Cliente removido com sucesso.");
        } catch (DataIntegrityViolationException e) {
        	attr.addFlashAttribute("erro", "Não é possível excluir o cliente devido a registros dependentes na tabela 'Entrada'.");
        }
        return "redirect:/clientes/lista";
    }
}
