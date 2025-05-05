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

import br.com.appcontrole.domain.cliente.Cliente;
import br.com.appcontrole.domain.cliente.ClienteService;

@Controller
@RequestMapping("/saidas")
public class SaidaController {

    @Autowired
    private SaidaService saidaService;
    
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/lista")
    public String listaSaidas(Model model) {
        List<Saida> saidas = saidaService.getDataSaidaDesc();
        model.addAttribute("saidas", saidas);
        return "saidas/lista";
    }
    
    @GetMapping("/editar/{id}")
    public String editarSaida(@PathVariable Long id, Model model) {
        Saida saida = saidaService.buscaPorId(id);
        model.addAttribute("saida", saida);
        return "saidas/editar"; // Página de edição de saída
    }

    @PostMapping("/editar/{id}")
    public String atualizarSaida(@PathVariable Long id, Saida saida, RedirectAttributes attr) {
    	// Busca a entrada existente pelo ID
        Saida saidaExistente = saidaService.buscaPorId(id);
        
        if (saidaExistente != null) {
            // Verifica se o cliente na entrada já possui um ID
            if (saida.getCliente() != null && saida.getCliente().getId() != null) {
                // Se o cliente já existe (tem ID), simplesmente atualiza a entrada com ele
            	saida.setId(id);
            	saidaService.atualiza(saida);
            } else {
                // Se o cliente na entrada não tem ID, precisamos verificar pelo nome
                Cliente clienteExistente = clienteService.buscaPorNome(saida.getCliente().getNome());
                
                if (clienteExistente != null) {
                    // Se encontrou o cliente pelo nome, associa ao objeto de entrada
                	saida.setCliente(clienteExistente);
                } else {
                    // Se não encontrou pelo nome, insere o novo cliente
                    Cliente novoCliente = clienteService.insere(saida.getCliente());
                    saida.setCliente(novoCliente);
                }
                
                // Atualiza a entrada com as alterações
                saida.setId(id);
                saidaService.atualiza(saida);
            }
            
            attr.addFlashAttribute("mensagem", "Entrada atualizada com sucesso.");
        } else {
            attr.addFlashAttribute("erro", "Entrada não encontrada.");
        }
        
        return "redirect:/saidas/lista"; // Redirecionar de volta para a lista de saídas após a atualização
    }

    @GetMapping("/remover/{id}")
    public String removerSaida(@PathVariable Long id) {
        saidaService.remove(id);
        return "redirect:/saidas/lista"; // Redirecionar de volta para a lista de saídas após a remoção
    }
}
