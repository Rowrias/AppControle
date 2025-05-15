package br.com.appcontrole.domain.funcionario;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	// Insere
	@PostMapping("/lista")
    public String novoFuncionario(Funcionario funcionario, Model model) {
		if (funcionario.getPassword() == null || funcionario.getPassword().isEmpty()) {
	        funcionario.setPassword("Não vai entrar pois precisa de 'username'"); // Defina uma senha padrão aqui
	    }
		funcionarioService.insere(funcionario);
        return "redirect:/funcionarios/lista";
    }
	
	// Lista com ordenação
    @GetMapping("/lista")
    public String listaFuncionario(Model model,
                       @RequestParam(name = "sortBy", required = false, defaultValue = "nome") String sortBy,
                       @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDirection) {

        Sort sort = Sort.by(sortBy);
        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }

        List<Funcionario> funcionarios = funcionarioService.buscaTodosOrdenado(sort);
        model.addAttribute("funcionarios", funcionarios);
        model.addAttribute("roles", Role.values());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        return "funcionarios/lista";
    }
	
	// Editar
	@GetMapping("/editar/{id}")
    public String editarEntrada(@PathVariable UUID id, Model model) {
		Funcionario funcionario = funcionarioService.buscaPorId(id);
        model.addAttribute("funcionario", funcionario);
        model.addAttribute("roles", Role.values());

        return "funcionarios/editar";
    }

    @PostMapping("/editar/{id}")
    public String atualizarEntrada(@PathVariable UUID id, Funcionario funcionario) {
    	funcionario.setId(id);
        funcionarioService.atualiza(funcionario);
        return "redirect:/funcionarios/lista";
    }

    // Remover
    @GetMapping("/remover/{id}")
    public String removerEntrada(@PathVariable UUID id) {
    	funcionarioService.remove(id);
        return "redirect:/funcionarios/lista";
    }
}
