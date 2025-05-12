package br.com.appcontrole.domain.saida;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/saidas")
public class SaidaController {

    @Autowired
    private SaidaService saidaService;

    // Lista
    @GetMapping("/lista")
    public String listaSaidas(Model model, 
			    		@RequestParam(name = "page", defaultValue = "0") int page,
			    	    @RequestParam(name = "size", defaultValue = "5") int size,
			    	    @RequestParam(name = "busca", required = false) String busca) {
        
    	Pageable pageable = PageRequest.of(page, size);
        Page<Saida> paginaSaidas;
    	
    	if (busca != null && !busca.isBlank()) {
            paginaSaidas = saidaService.buscarPorDestino(busca, pageable);
        } else {
            paginaSaidas = saidaService.listarPaginado(pageable);
        }
    	
    	model.addAttribute("paginaSaidas", paginaSaidas);
        model.addAttribute("busca", busca); // mantém o valor da busca no campo de texto
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
