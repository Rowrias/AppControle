package br.com.appcontrole.domain.cliente;

import java.util.List;

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
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping("/lista")
    public String listaClientes(Model model) {
		List<Cliente> cliente = clienteService.buscaTodosOrdenadoPorNome();
        model.addAttribute("clientes", cliente);
        return "clientes/lista";
    }
	
	@PostMapping("/lista")
    public String novoCliente(Cliente cliente, RedirectAttributes attr) {
		
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
	
	@GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
    	Cliente cliente = clienteService.buscaPorId(id);
        model.addAttribute("cliente", cliente);
        return "clientes/editar";
    }

	@PostMapping("/editar/{id}")
    public String atualizarCliente(@PathVariable Long id, Cliente cliente, RedirectAttributes attr) {
        // Verifica se existe outro cliente com o mesmo nome, exceto o cliente atual
        Cliente clienteExistente = clienteService.buscaPorNome(cliente.getNome());
        if (clienteExistente != null && !clienteExistente.getId().equals(id)) {
            attr.addFlashAttribute("erro", "Já existe um cliente com esse nome.");
            return "redirect:/clientes";
        }
        
        cliente.setId(id);
        clienteService.atualiza(cliente);
        attr.addFlashAttribute("mensagem", "Cliente atualizado com sucesso.");
        return "redirect:/clientes/lista";
    }
    
    @GetMapping("/remover/{id}")
    public String removerCliente(@PathVariable Long id, RedirectAttributes attr) {
    	try {
            clienteService.remove(id);
            attr.addFlashAttribute("mensagem", "Cliente removido com sucesso.");
        } catch (DataIntegrityViolationException e) {
        	attr.addFlashAttribute("erro", "Não é possível excluir o cliente devido a registros dependentes na tabela 'saida'.");
        }
        return "redirect:/clientes/lista";
    }
}
