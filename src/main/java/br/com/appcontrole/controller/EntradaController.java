package br.com.appcontrole.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.appcontrole.model.Entrada;
import br.com.appcontrole.model.Saida;
import br.com.appcontrole.service.EntradaService;
import br.com.appcontrole.service.SaidaService;

@Controller
public class EntradaController {

    @Autowired
    private EntradaService entradaService;
    
    @Autowired
    private SaidaService saidaService;

    @PostMapping("/entradas")
    public String novaEntrada(Entrada entrada, Model model) {
    	entradaService.insere(entrada);
        return "redirect:/entradas";
    }
    
    @GetMapping("/entradas")
    public String listaEntradas(Model model) {
        List<Entrada> pendentes = entradaService.buscaPorStatus(false);
        List<Entrada> concluidas = entradaService.buscaPorStatus(true);
        // Exibe os dados separando em listas diferentes (pendentes e concluídas)
        model.addAttribute("pendentes", pendentes);
        model.addAttribute("concluidas", concluidas);

        // Exibe os dados pelo ordem inversa do Id
        model.addAttribute("pendentes", entradaService.getPendentesDataHoraDesc());
        model.addAttribute("concluidas", entradaService.getConcluidasDataHoraDesc());

        return "entradas/entrada";
    }
    
    @GetMapping("/entradas/concluido/{id}")
    public String alteraStatusConcluido(@PathVariable Long id) {
        Entrada entrada = entradaService.buscaPorId(id);
        if (entrada != null) {
            entrada.setConcluido(!entrada.isConcluido());
            entrada.setDataConcluido(LocalDateTime.now());
            entradaService.atualiza(entrada);
        }
        return "redirect:/entradas";
    }
        
    @GetMapping("/entradas/moverParaSaida/{id}")
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
        return "redirect:/entradas";
    }
    
    @GetMapping("/entradas/editar/{id}")
    public String editarEntrada(@PathVariable Long id, Model model) {
    	Entrada entrada = entradaService.buscaPorId(id);
        model.addAttribute("entrada", entrada);
        return "entrada/editar";
    }

    @PostMapping("/entradas/editar/{id}")
    public String atualizarEntrada(@PathVariable Long id, Entrada entrada) {
        entrada.setId(id);
        entradaService.atualiza(entrada);
        return "redirect:/entradas";
    }

    @GetMapping("/entradas/remover/{id}")
    public String removerEntrada(@PathVariable Long id) {
        entradaService.remove(id);
        return "redirect:/entradas";
    }
   
}
