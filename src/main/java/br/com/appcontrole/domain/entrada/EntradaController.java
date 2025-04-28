package br.com.appcontrole.domain.entrada;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.appcontrole.domain.cliente.Cliente;
import br.com.appcontrole.domain.cliente.ClienteService;
import br.com.appcontrole.domain.saida.Saida;
import br.com.appcontrole.domain.saida.SaidaService;

@Controller
@RequestMapping("/entradas")
public class EntradaController {

    @Autowired
    private EntradaService entradaService;
    
    @Autowired
    private SaidaService saidaService;
    
    @Autowired
    private ClienteService clienteService;

    @PostMapping("/lista")
    public String novaEntrada(@ModelAttribute("entrada") Entrada entrada) {
        
        Cliente cliente = clienteService.buscaPorNome(entrada.getCliente().getNome());
        
        // Verifica se o cliente já existe no banco de dados
        if (cliente == null) {
            // Se não existe, salva o cliente antes de associá-lo à entrada
            cliente = clienteService.insere(entrada.getCliente());
        }
        
        // Associa o cliente persistido à entrada
        entrada.setCliente(cliente);
        // Salva a entrada
        entradaService.insere(entrada);
        
        return "redirect:/entradas/lista";
    }
    
    @GetMapping("/lista")
    public String listaEntradas(Model model) {
        List<Entrada> pendentes = entradaService.buscaPorStatus(false);
        List<Entrada> concluidas = entradaService.buscaPorStatus(true);
        // Exibe os dados separando em listas diferentes (pendentes e concluídas)
        model.addAttribute("pendentes", pendentes);
        model.addAttribute("concluidas", concluidas);

        // Exibe os dados pelo ordem inversa do Id
        model.addAttribute("pendentes", entradaService.getPendentesDataHoraDesc());
        model.addAttribute("concluidas", entradaService.getConcluidasDataHoraDesc());

        return "entradas/lista";
    }
    
    @GetMapping("/concluido/{id}")
    public String alteraStatusConcluido(@PathVariable Long id) {
        Entrada entrada = entradaService.buscaPorId(id);
        if (entrada != null) {
            entrada.setConcluido(!entrada.isConcluido());
            if (entrada.isConcluido()) {
                entrada.setDataConcluido(LocalDateTime.now());
            } else {
                entrada.setDataConcluido(null);
            }
            entradaService.atualiza(entrada);
        }
        return "redirect:/entradas/lista";
    }
        
    @GetMapping("/moverParaSaida/{id}")
    public String moverParaSaida(@PathVariable Long id) {
        Entrada entrada = entradaService.buscaPorId(id);
        if (entrada != null && entrada.isConcluido()) {
            Saida saida = new Saida();
            saida.setCliente(entrada.getCliente());
            saida.setProduto(entrada.getProduto());
            saida.setQuantidade(entrada.getQuantidade());
            saida.setValorUnitario(entrada.getValorUnitario());
            saida.setValorTotal(entrada.getValorTotal());
            saida.setDataEntrada(entrada.getDataEntrada());
            saida.setDataConcluido(entrada.getDataConcluido());
            saida.setDataSaida(LocalDateTime.now());
            
            saidaService.insere(saida);
            entradaService.remove(id);
        }
        return "redirect:/entradas/lista";
    }
    
    @GetMapping("/editar/{id}")
    public String editarEntrada(@PathVariable Long id, Model model) {
        Entrada entrada = entradaService.buscaPorId(id);
        List<Cliente> clientes = clienteService.buscaTodos();
        
        model.addAttribute("entrada", entrada);
        model.addAttribute("clientes", clientes);
        
        return "entradas/editar";
    }

    @PostMapping("/editar/{id}")
    public String atualizarEntrada(@PathVariable Long id, Entrada entrada, RedirectAttributes attr) {
        // Busca a entrada existente pelo ID
        Entrada entradaExistente = entradaService.buscaPorId(id);
        
        if (entradaExistente != null) {
            // Verifica se o cliente na entrada já possui um ID
            if (entrada.getCliente() != null && entrada.getCliente().getId() != null) {
                // Se o cliente já existe (tem ID), simplesmente atualiza a entrada com ele
                entrada.setId(id);
                entradaService.atualiza(entrada);
            } else {
                // Se o cliente na entrada não tem ID, precisamos verificar pelo nome
                Cliente clienteExistente = clienteService.buscaPorNome(entrada.getCliente().getNome());
                
                if (clienteExistente != null) {
                    // Se encontrou o cliente pelo nome, associa ao objeto de entrada
                    entrada.setCliente(clienteExistente);
                } else {
                    // Se não encontrou pelo nome, insere o novo cliente
                    Cliente novoCliente = clienteService.insere(entrada.getCliente());
                    entrada.setCliente(novoCliente);
                }
                
                // Atualiza a entrada com as alterações
                entrada.setId(id);
                entradaService.atualiza(entrada);
            }
            
            attr.addFlashAttribute("mensagem", "Entrada atualizada com sucesso.");
        } else {
            attr.addFlashAttribute("erro", "Entrada não encontrada.");
        }
        
        return "redirect:/entradas/lista";
    }

    @GetMapping("/remover/{id}")
    public String removerEntrada(@PathVariable Long id, RedirectAttributes attr) {
        entradaService.remove(id);
        attr.addFlashAttribute("mensagem", "Entrada removida com sucesso.");
        return "redirect:/entradas/lista";
    }
   
}
