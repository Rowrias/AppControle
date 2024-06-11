package br.com.appcontrole.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.appcontrole.model.Funcionario;
import br.com.appcontrole.service.FuncionarioService;

@Controller
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@GetMapping("/funcionarios")
    public String listaFuncionario(Model model) {
        List<Funcionario> funcionario = funcionarioService.buscaTodos();
        model.addAttribute("funcionarios", funcionario);
        return "funcionarios/funcionario";
    }
	
	@PostMapping("/funcionarios")
    public String novoFuncionario(Funcionario funcionario, Model model) {
		funcionarioService.insere(funcionario);
        return "redirect:/funcionarios";
    }
	
	@GetMapping("/funcionarios/editar/{id}")
    public String editarEntrada(@PathVariable Long id, Model model) {
		Funcionario funcionario = funcionarioService.buscaPorId(id);
        model.addAttribute("funcionario", funcionario);
        return "funcionario/editar";
    }

    @PostMapping("/funcionarios/editar/{id}")
    public String atualizarEntrada(@PathVariable Long id, Funcionario funcionario) {
    	funcionario.setId(id);
        funcionarioService.atualiza(funcionario);
        return "redirect:/funcionarios";
    }

    @GetMapping("/funcionarios/remover/{id}")
    public String removerEntrada(@PathVariable Long id) {
    	funcionarioService.remove(id);
        return "redirect:/funcionarios";
    }
}
