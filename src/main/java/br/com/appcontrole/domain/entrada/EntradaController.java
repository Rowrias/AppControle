package br.com.appcontrole.domain.entrada;

import java.math.BigDecimal;
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
import br.com.appcontrole.domain.produto.Produto;
import br.com.appcontrole.domain.produto.ProdutoService;
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
    
    @Autowired
    private ProdutoService produtoService; 

    // Insere
    @PostMapping("/lista")
    public String novaEntrada(@ModelAttribute Entrada entrada) {

    	// --- CLIENTE ---
        String nomeCliente = entrada.getCliente().getNome().trim().toLowerCase();
        Cliente clienteExistente = clienteService.findByNomeIgnoreCase(nomeCliente);
        if (clienteExistente == null) {
            Cliente novoCliente = new Cliente();
            novoCliente.setNome(nomeCliente);
            clienteExistente = clienteService.insere(novoCliente);
        }

        entrada.setCliente(clienteExistente);

        // --- PRODUTO ---
        String nomeProduto = entrada.getProduto().getNome().trim().toLowerCase();
        Produto produtoExistente = produtoService.findByNomeIgnoreCase(nomeProduto);
        if (produtoExistente == null) {
            Produto novoProduto = new Produto();
            novoProduto.setNome(nomeProduto);
            novoProduto.setQuantidade(0);
            produtoExistente = produtoService.insere(novoProduto);
        }
        
        // Atualiza o estoque do produto
        produtoExistente.setQuantidade(produtoExistente.getQuantidade() + entrada.getQuantidade());  // Adiciona a quantidade na entrada
        produtoService.insere(produtoExistente); // Salva o produto com a quantidade atualizada

        entrada.setProduto(produtoExistente);

        // --- DATA E FUNCIONÁRIO ---
        entrada.setDataEntrada(LocalDateTime.now());

        // O Funcionario é atribuído diretamente dentro do Service
        entradaService.insere(entrada);  // Salva a entrada no banco de dados

        return "redirect:/entradas/lista";
    }

    // Lista
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

    // Edita
    @GetMapping("/editar/{id}")
    public String editarEntrada(@PathVariable Long id, Model model) {
        Entrada entrada = entradaService.buscaPorId(id);
        List<Cliente> clientes = clienteService.buscaTodos();
        List<Produto> produtos = produtoService.buscaTodos();
        
        model.addAttribute("entrada", entrada);
        model.addAttribute("clientes", clientes);
        model.addAttribute("produtos", produtos);
        
        return "entradas/editar";
    }

    @PostMapping("/editar/{id}")
    public String atualizarEntrada(@PathVariable Long id, Entrada entrada, RedirectAttributes attr) {
    	// --- CLIENTE ---
        String nomeCliente = entrada.getCliente().getNome().trim().toLowerCase();
        Cliente clienteExistente = clienteService.findByNomeIgnoreCase(nomeCliente);
        if (clienteExistente == null) {
            Cliente novoCliente = new Cliente();
            novoCliente.setNome(nomeCliente);
            clienteExistente = clienteService.insere(novoCliente);
        }

        entrada.setCliente(clienteExistente);

        // --- PRODUTO ---
        String nomeProduto = entrada.getProduto().getNome().trim().toLowerCase();
        Produto produtoExistente = produtoService.findByNomeIgnoreCase(nomeProduto);
        if (produtoExistente == null) {
            Produto novoProduto = new Produto();
            novoProduto.setNome(nomeProduto);
            novoProduto.setQuantidade(0);
            produtoExistente = produtoService.insere(novoProduto);
        }
        
        // Atualiza o estoque do produto
        // Busca a entrada original do banco
        Entrada entradaOriginal = entradaService.buscaPorId(id);
        Produto produtoOriginal = entradaOriginal.getProduto();

        // Subtrai a quantidade antiga do estoque
        produtoOriginal.setQuantidade(produtoOriginal.getQuantidade() - entradaOriginal.getQuantidade());
        produtoService.insere(produtoOriginal);
        
        // Atualiza o novo produto com a nova quantidade
        produtoExistente.setQuantidade(produtoExistente.getQuantidade() + entrada.getQuantidade());
        produtoService.insere(produtoExistente);

        entrada.setProduto(produtoExistente);

        // --- DATA E FUNCIONÁRIO ---
        entrada.setDataEntrada(LocalDateTime.now());

        // O Funcionario é atribuído diretamente dentro do Service
        entradaService.insere(entrada);  // Salva a entrada no banco de dados
        attr.addFlashAttribute("mensagem", "Entrada editada com sucesso.");
        
        return "redirect:/entradas/lista";
    }

    // Remove
    @GetMapping("/remover/{id}")
    public String removerEntrada(@PathVariable Long id, RedirectAttributes attr) {
    	Entrada entrada = entradaService.buscaPorId(id);
        if (entrada != null) {
            Produto produto = entrada.getProduto();
            if (produto != null) {
                // Subtrai a quantidade da entrada do estoque do produto
                int novaQuantidade = produto.getQuantidade() - entrada.getQuantidade();
                produto.setQuantidade(Math.max(novaQuantidade, 0));  // Evita estoque negativo
                produtoService.insere(produto);
            }

            entradaService.remove(id);
            attr.addFlashAttribute("mensagem", "Entrada removida com sucesso.");
        }
        return "redirect:/entradas/lista";
    }
    
    // -----------------------------------------------------------
    
    // Altera para concluído
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
    
    // Mover para saida
    @GetMapping("/moverParaSaida/{id}")
    public String moverParaSaida(@PathVariable Long id) {
        Entrada entrada = entradaService.buscaPorId(id);
        
        if (entrada != null && entrada.isConcluido()) {
        	Produto produto = entrada.getProduto();
        	
            Saida saida = new Saida();
            saida.setCliente(entrada.getCliente());
            saida.setProduto(entrada.getProduto());
            saida.setQuantidade(entrada.getQuantidade());

            // Valor unitário e total
            if (produto != null && produto.getValorUnitario() != null) {
            	BigDecimal valorUnitario = produto.getValorUnitario();
            	BigDecimal valorTotal = valorUnitario.multiply(BigDecimal.valueOf(entrada.getQuantidade()));

                saida.setValorUnitario(valorUnitario);
                saida.setValorTotal(valorTotal);
            }
            
            saida.setDataEntrada(entrada.getDataEntrada());
            saida.setDataConcluido(entrada.getDataConcluido());
            saida.setDataSaida(LocalDateTime.now());
            saida.setFuncionario(entrada.getFuncionario());

            if (produto != null) {
                // Subtrai do estoque a quantidade movimentada
                int novaQuantidade = produto.getQuantidade() - entrada.getQuantidade();
                produto.setQuantidade(Math.max(novaQuantidade, 0));  // Evita estoque negativo
                produtoService.insere(produto);
            }

            saidaService.insere(saida);
            entradaService.remove(id);
        }
        return "redirect:/entradas/lista";
    }
   
}
