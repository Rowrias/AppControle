package br.com.appcontrole.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.appcontrole.model.Cliente;
import br.com.appcontrole.service.ClienteService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping("/lista")
    public String listaClientes(Model model) {
        List<Cliente> cliente = clienteService.buscaTodos();
        model.addAttribute("clientes", cliente);
        return "clientes/lista";
    }
	
	@PostMapping("/lista")
    public String novoCliente(Cliente cliente, Model model) {
		clienteService.insere(cliente);
        return "redirect:/clientes/lista";
    }
	
	@GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
    	Cliente cliente = clienteService.buscaPorId(id);
        model.addAttribute("cliente", cliente);
        return "clientes/editar";
    }

    @PostMapping("/editar/{id}")
    public String atualizarCliente(@PathVariable Long id, Cliente cliente) {
        cliente.setId(id);
        clienteService.atualiza(cliente);
        return "redirect:/clientes/lista";
    }
    
    @GetMapping("/remover/{id}")
    public String removerClientes(@PathVariable Long id) {
    	clienteService.remove(id);
        return "redirect:/clientes/lista";
    }
}
