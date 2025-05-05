package br.com.appcontrole.domain.funcionario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@GetMapping("/lista")
    public String listaFuncionario(Model model) {
        List<Funcionario> funcionario = funcionarioService.buscaTodosOrdenadoPorNome();
        model.addAttribute("funcionarios", funcionario);
        model.addAttribute("roles", Role.values());

        return "funcionarios/lista";
    }
	
	@PostMapping("/lista")
    public String novoFuncionario(Funcionario funcionario, Model model) {
		if (funcionario.getPassword() == null || funcionario.getPassword().isEmpty()) {
	        funcionario.setPassword("Não vai entrar pois precisa de 'username'"); // Defina uma senha padrão aqui
	    }
		funcionarioService.insere(funcionario);
        return "redirect:/funcionarios/lista";
    }
	
	@GetMapping("/editar/{id}")
    public String editarEntrada(@PathVariable Long id, Model model) {
		Funcionario funcionario = funcionarioService.buscaPorId(id);
        model.addAttribute("funcionario", funcionario);
        model.addAttribute("roles", Role.values());

        return "funcionarios/editar";
    }

    @PostMapping("/editar/{id}")
    public String atualizarEntrada(@PathVariable Long id, Funcionario funcionario) {
    	funcionario.setId(id);
        funcionarioService.atualiza(funcionario);
        return "redirect:/funcionarios/lista";
    }

    @GetMapping("/remover/{id}")
    public String removerEntrada(@PathVariable Long id) {
    	funcionarioService.remove(id);
        return "redirect:/funcionarios/lista";
    }
}
