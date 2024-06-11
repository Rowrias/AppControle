package br.com.appcontrole.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.appcontrole.model.Saida;
import br.com.appcontrole.service.SaidaService;

import java.util.List;

@Controller
@RequestMapping("/saidas")
public class SaidaController {

    @Autowired
    private SaidaService saidaService;

    @GetMapping("/lista")
    public String listaSaidas(Model model) {
        List<Saida> saidas = saidaService.buscaTodos();
        model.addAttribute("saidas", saidas);
        return "saidas/lista";
    }
    
    @GetMapping("/editar/{id}")
    public String editarSaida(@PathVariable Long id, Model model) {
        Saida saida = saidaService.buscaPorId(id);
        model.addAttribute("saida", saida);
        return "saidas/lista"; // Página de edição de saída
    }

    @PostMapping("/editar/{id}")
    public String atualizarSaida(@PathVariable Long id, Saida saida) {
        saida.setId(id); // Garanta que o ID correto esteja definido para a atualização
        saidaService.atualiza(saida);
        return "redirect:/saidas/lista"; // Redirecionar de volta para a lista de saídas após a atualização
    }

    @GetMapping("/remover/{id}")
    public String removerSaida(@PathVariable Long id) {
        saidaService.remove(id);
        return "redirect:/saidas/lista"; // Redirecionar de volta para a lista de saídas após a remoção
    }
}
