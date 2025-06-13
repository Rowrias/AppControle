package br.com.appcontrole.domain.saida;

import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        // --- Adicionado: Inicializa um novo objeto Saida para o formulário de cadastro ---
        // Se o objeto 'saida' já vier de um RedirectAttributes (após um erro de validação),
        // ele será mantido no modelo. Caso contrário, um novo objeto Saida vazio é criado.
        if (!model.containsAttribute("saida")) {
            model.addAttribute("saida", new Saida());
        }
        
        return "saidas/lista";
    }
    
    // Editar
    @GetMapping("/editar/{id}")
    public String editarSaida(@PathVariable UUID id, Model model) {
    	if (!model.containsAttribute("saida")) {
            Saida saida = saidaService.buscaPorId(id);
            model.addAttribute("saida", saida);
        }
        return "saidas/editar";
    }

    @PostMapping("/editar/{id}")
    public String atualizarSaida(@PathVariable UUID id, 
    		@Valid @ModelAttribute("saida") Saida saida,
    		BindingResult result,
    		RedirectAttributes attr) {
    	
    	// É crucial setar o ID no objeto 'saida' que veio do formulário,
        // pois o ID vem do PathVariable e não do formulário diretamente.
        saida.setId(id);
    	
     // --- Validação ---
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.saida", result);
            attr.addFlashAttribute("saida", saida); // Para manter os dados preenchidos

            StringJoiner errorMessage = new StringJoiner(", ");
            result.getAllErrors().forEach(error -> errorMessage.add(error.getDefaultMessage()));
            attr.addFlashAttribute("erro", "Erro de validação: " + errorMessage.toString());

            // Redireciona de volta para a página de edição para mostrar os erros
            return "redirect:/saidas/editar/" + id;
        }
    	
        // Lógica de atualização existente
        Saida saidaExistente = saidaService.buscaPorId(id); // Buscar para garantir que existe

        if (saidaExistente != null) {
            saidaService.atualiza(saida); // Atualiza o objeto
            attr.addFlashAttribute("mensagem", "Saída atualizada com sucesso.");
        } else {
            // Caso, por algum motivo, a saída não seja encontrada ao tentar atualizar
            attr.addFlashAttribute("erro", "Saída não encontrada para atualização.");
        }

        return "redirect:/saidas/lista";
    }

    // Remover
    @GetMapping("/remover/{id}")
    public String removerSaida(@PathVariable UUID id, RedirectAttributes attr) {
    	try {
            saidaService.remove(id);
            attr.addFlashAttribute("mensagem", "Saída removida com sucesso.");
        } catch (DataIntegrityViolationException e) {
            // Adicionado: Tratamento para erro de integridade referencial
            // Isso acontece se você tentar remover uma Saída que está ligada a outros registros (ex: relatório).
            attr.addFlashAttribute("erro", "Não é possível excluir a saída devido a registros dependentes.");
        }
        return "redirect:/saidas/lista";
    }
}
