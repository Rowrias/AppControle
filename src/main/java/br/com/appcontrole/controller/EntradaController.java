package br.com.appcontrole.controller;

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

import br.com.appcontrole.model.Cliente;
import br.com.appcontrole.model.Entrada;
import br.com.appcontrole.model.Saida;
import br.com.appcontrole.service.ClienteService;
import br.com.appcontrole.service.EntradaService;
import br.com.appcontrole.service.SaidaService;

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
    public String novaEntrada(@ModelAttribute("entrada") Entrada entrada, Model model) {
        // Verifica se o cliente já existe no banco de dados
        Cliente cliente = clienteService.clienteExiste(entrada.getCliente().getNome());
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
            saida.setCliente(entrada.getCliente().getNome());
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
    public String atualizarEntrada(@PathVariable Long id, Entrada entrada) {
        entrada.setId(id);
        entradaService.atualiza(entrada);
        return "redirect:/entradas/lista";
    }

    @GetMapping("/remover/{id}")
    public String removerEntrada(@PathVariable Long id) {
        entradaService.remove(id);
        return "redirect:/entradas/lista";
    }
   
}
