package br.com.appcontrole.domain.saida;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "buscaCliente", required = false) String buscaCliente,
            @RequestParam(name = "buscaProduto", required = false) String buscaProduto,
            @RequestParam(name = "sortBy", defaultValue = "dataSaida") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection) {
        
    	// Criação do Pageable uma única vez
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
    	
        Page<Saida> paginaSaidas;
        
        // Lógica para aplicar a busca de forma combinada ou individual
        boolean temBuscaCliente = buscaCliente != null && !buscaCliente.isBlank();
        boolean temBuscaProduto = buscaProduto != null && !buscaProduto.isBlank();

    	
        if (temBuscaCliente && temBuscaProduto) {
            paginaSaidas = saidaService.buscarPorClienteEProduto(buscaCliente, buscaProduto, pageable);
        } else if (temBuscaCliente) {
            paginaSaidas = saidaService.buscarPorCliente(buscaCliente, pageable);
        } else if (temBuscaProduto) {
            paginaSaidas = saidaService.buscarPorProduto(buscaProduto, pageable);
        } else {
            paginaSaidas = saidaService.listarPaginado(pageable);
        }
    	
        model.addAttribute("paginaSaidas", paginaSaidas);
        
        // Manter os termos de busca no modelo para os inputs e links de ordenação/paginação
        // Isto é crucial para que os campos de busca e os links de ordenação mantenham o estado
        model.addAttribute("buscaCliente", buscaCliente);
        model.addAttribute("buscaProduto", buscaProduto);
        
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        
        return "saidas/lista";
    }
    
    // Editar
    @GetMapping("/editar/{id}")
    public String editarSaida(@PathVariable UUID id, Model model) {
    	Saida saida = saidaService.buscaPorId(id);
    	
        model.addAttribute("saida", saida);
        
        return "saidas/editar";
    }

    @PostMapping("/editar/{id}")
    public String atualizarSaida(@PathVariable UUID id, Saida saida, RedirectAttributes attr) {
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
    public String removerSaida(@PathVariable UUID id) {
        saidaService.remove(id);
        return "redirect:/saidas/lista";
    }
}
