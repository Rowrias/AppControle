package br.com.appcontrole.domain.saida;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/saidas")
public class SaidaController {

    @Autowired
    private SaidaService saidaService;

    // Lista
    @GetMapping("/lista")
    public String listaSaidas(Model model) {
        List<Saida> saidas = saidaService.getDataSaidaDesc();
        model.addAttribute("saidas", saidas);
        return "saidas/lista";
    }
    
    // Editar
    @GetMapping("/editar/{id}")
    public String editarSaida(@PathVariable Long id, Model model) {
        Saida saida = saidaService.buscaPorId(id);
        model.addAttribute("saida", saida);
        return "saidas/editar";
    }

    @PostMapping("/editar/{id}")
    public String atualizarSaida(@PathVariable Long id, Saida saida, RedirectAttributes attr) {
        Saida saidaExistente = saidaService.buscaPorId(id);

        if (saidaExistente != null) {
            saida.setId(id);
            saidaService.atualiza(saida);
            attr.addFlashAttribute("mensagem", "Saída atualizada com sucesso.");
        } else {
            attr.addFlashAttribute("erro", "Saída não encontrada.");
        }

        return "redirect:/saidas/lista";
    }

    // Remover
    @GetMapping("/remover/{id}")
    public String removerSaida(@PathVariable Long id) {
        saidaService.remove(id);
        return "redirect:/saidas/lista";
    }
}
