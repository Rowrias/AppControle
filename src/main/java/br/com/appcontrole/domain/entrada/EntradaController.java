package br.com.appcontrole.domain.entrada;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.appcontrole.domain.cliente.Cliente;
import br.com.appcontrole.domain.cliente.ClienteService;
import br.com.appcontrole.domain.funcionario.Funcionario;
import br.com.appcontrole.domain.funcionario.FuncionarioService;
import br.com.appcontrole.domain.produto.Produto;
import br.com.appcontrole.domain.produto.ProdutoService;
import br.com.appcontrole.domain.saida.Saida;
import br.com.appcontrole.domain.saida.SaidaService;
import jakarta.validation.Valid;

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
    private FuncionarioService funcionarioService;
    
    @Autowired
    private ProdutoService produtoService;
    
    // Insere
    @PostMapping("/lista")
    public String novaEntrada(@Valid @ModelAttribute("entrada") Entrada entrada,
					    		BindingResult result,
					    		RedirectAttributes attr) {
		
    	// Verifica erros de validação
    	if (result.hasErrors()) {
	    	// Adiciona o BindingResult e o objeto 'entrada' como flash attributes
			attr.addFlashAttribute("org.springframework.validation.BindingResult.entrada", result);
			attr.addFlashAttribute("entrada", entrada); // Para preservar os dados no formulário
            attr.addFlashAttribute("erro", "Verifique os dados da entrada e tente novamente."); // Mensagem genérica
			return "redirect:/entradas/lista";
    	}

    	// --- CLIENTE ---
    	// Remove espaços em branco do nome do cliente e converte para minúsculas
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
            novoProduto.setQuantidade(0); // Inicializa quantidade para novo produto
            produtoExistente = produtoService.insere(novoProduto);
        }
        
        // Atualiza o estoque do produto
        produtoExistente.setQuantidade(produtoExistente.getQuantidade() + entrada.getQuantidade());  // Adiciona a quantidade na entrada
        produtoService.insere(produtoExistente); // Salva o produto com a quantidade atualizada

        entrada.setProduto(produtoExistente);
        
        // --- ATRIBUIR FUNCIONÁRIO LOGADO NA CRIAÇÃO ---
        Funcionario funcionarioLogado = getFuncionarioLogado();
        if (funcionarioLogado != null) {
            entrada.setFuncionario(funcionarioLogado);
        } else {
            // Opcional: Tratar caso o funcionário não seja encontrado (talvez um erro ou um funcionário padrão)
            // attr.addFlashAttribute("erro", "Não foi possível identificar o funcionário logado.");
            // return "redirect:/entradas/lista";
        }

        entradaService.insere(entrada);  // Salva a entrada no banco de dados
        attr.addFlashAttribute("mensagem", "Entrada adicionada com sucesso.");

        return "redirect:/entradas/lista";
    }

    // Lista
    @GetMapping("/lista")
    public String listaEntradas(Model model,
					@RequestParam(name = "sortBy", required = false) String sortBy,
					@RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection) {
        
    	List<Entrada> pendentes;
        List<Entrada> concluidas;
        
        if (sortBy != null && !sortBy.isEmpty()) {
            pendentes = entradaService.buscaPorStatusOrdenado(false, sortBy, sortDirection);
            concluidas = entradaService.buscaPorStatusOrdenado(true, sortBy, sortDirection);
        } else {
            pendentes = entradaService.getPendentesDataHoraDesc();
            concluidas = entradaService.getConcluidasDataHoraDesc();
        }

        model.addAttribute("pendentes", pendentes);
        model.addAttribute("concluidas", concluidas);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        
        // Se a 'entrada' já não estiver no modelo (vindo de um flash attribute após erro de validação),
        // adicione um novo objeto Entrada para o formulário de adição.
        if (!model.containsAttribute("entrada")) {
            model.addAttribute("entrada", new Entrada());
        }
        
        return "entradas/lista";
    }

    // Edita
    @GetMapping("/editar/{id}")
    public String editarEntrada(@PathVariable UUID id, Model model) {
    	// Se a 'entrada' já não estiver no modelo (vindo de um flash attribute após erro de validação),
        // busque a entrada pelo ID.
        if (!model.containsAttribute("entrada")) {
            Entrada entrada = entradaService.buscaPorId(id);
            model.addAttribute("entrada", entrada);
        }
        
        // Listas de clientes e produtos são para dropdowns, mas seu HTML usa autocomplete
        // De qualquer forma, mantemos elas aqui, caso o front-end mude ou você as use para algo
        List<Cliente> clientes = clienteService.buscaTodos();
        List<Produto> produtos = produtoService.buscaTodos();
    	
        model.addAttribute("clientes", clientes);
        model.addAttribute("produtos", produtos);
        
        return "entradas/editar";
    }
    	
    @PostMapping("/editar/{id}")
    public String atualizarEntrada(@PathVariable UUID id,
    								@Valid @ModelAttribute("entrada") Entrada entrada, 
						    		BindingResult result,
    								RedirectAttributes attr,
    								Model model) {
        
    	// Define o ID da entrada no objeto 'entradaAtualizada' para que a validação e atualização funcionem
    	entrada.setId(id);
    	
    	// Verifica erros de validação do Bean Validation primeiro
        if (result.hasErrors()) {
        	Entrada entradaOriginal = entradaService.buscaPorId(id); // Recarrega a entrada original
        	
        	// Reassocia o funcionario, cliente e produto originais para exibir no formulário em caso de erro
            if (entradaOriginal != null) {
                entrada.setFuncionario(entradaOriginal.getFuncionario());
                entrada.setCliente(entradaOriginal.getCliente());
                entrada.setProduto(entradaOriginal.getProduto());
            }
            
            model.addAttribute("entrada", entrada);
            model.addAttribute("erro", "Verifique os dados da entrada e tente novamente."); // Mensagem de erro

            // Adicione as listas de clientes e produtos novamente para os dropdowns (se usados)
            model.addAttribute("clientes", clienteService.buscaTodos());
            model.addAttribute("produtos", produtoService.buscaTodos());

            return "entradas/editar";
        	
        }
    	
    	
    	// --- Buscar entrada original para comparar e ajustar estoque ---
        Entrada entradaOriginal = entradaService.buscaPorId(id);
        Produto produtoOriginalDaEntrada  = entradaOriginal.getProduto();
        
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
        Produto produtoAtualizadoEntrada  = produtoService.findByNomeIgnoreCase(nomeProduto);
        if (produtoAtualizadoEntrada  == null) {
            Produto novoProduto = new Produto();
            novoProduto.setNome(nomeProduto);
            novoProduto.setQuantidade(0);
            produtoAtualizadoEntrada  = produtoService.insere(novoProduto);
        }
        
        // Se o produto mudou ou a quantidade original é diferente, ajuste o estoque
        if (!produtoOriginalDaEntrada.getId().equals(produtoAtualizadoEntrada.getId())) {
        	// Se o produto mudou, remove do estoque do produto antigo e adiciona ao novo
            produtoOriginalDaEntrada.setQuantidade(produtoOriginalDaEntrada.getQuantidade() - entradaOriginal.getQuantidade());
            produtoService.insere(produtoOriginalDaEntrada);

            produtoAtualizadoEntrada.setQuantidade(produtoAtualizadoEntrada.getQuantidade() + entrada.getQuantidade());
            produtoService.insere(produtoAtualizadoEntrada);
        	
        } else {
            // Se o produto é o mesmo, ajusta apenas a diferença de quantidade
            int diferencaQuantidade = entrada.getQuantidade() - entradaOriginal.getQuantidade();
            produtoAtualizadoEntrada.setQuantidade(produtoAtualizadoEntrada.getQuantidade() + diferencaQuantidade);
            produtoService.insere(produtoAtualizadoEntrada);
        }
        entrada.setProduto(produtoAtualizadoEntrada);
        
        // --- ATRIBUIR FUNCIONÁRIO LOGADO NA ATUALIZAÇÃO ---
        Funcionario funcionarioLogado = getFuncionarioLogado();
        if (funcionarioLogado != null) {
            entrada.setFuncionario(funcionarioLogado); // SOBRESCREVE o funcionário original
        } else {
            // Opcional: Se não encontrar o funcionário logado, pode manter o original
            entrada.setFuncionario(entradaOriginal.getFuncionario());
        }

        // --- Preserva dados da entrada original que não são editáveis no formulário ---
        entrada.setConcluido(entradaOriginal.isConcluido());
        if (entradaOriginal.getDataConcluido() != null) { // Preserva a data de conclusão se já existia
        	entrada.setDataConcluido(entradaOriginal.getDataConcluido());
    	}
        
        // --- Atualiza entrada ---
        entradaService.atualiza(entrada);

        attr.addFlashAttribute("mensagem", "Entrada atualizada com sucesso.");
        return "redirect:/entradas/lista";
    }

    // Remove
    @GetMapping("/remover/{id}")
    public String removerEntrada(@PathVariable UUID id, RedirectAttributes attr) {
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
        } else {
        	attr.addFlashAttribute("erro", "Entrada não encontrada para remoção.");
        }
        return "redirect:/entradas/lista";
    }
    
    // -----------------------------------------------------------
    
    // Autocomplete de cliente
    @GetMapping("/autocomplete/clientes")
    @ResponseBody
    public List<String> autocompleteClientes(@RequestParam("query") String query) {
        return clienteService.findByNomeContainingIgnoreCase(query).stream()
                .map(Cliente::getNome)
                .collect(Collectors.toList());
    }

    // Autocomplete de produto
    @GetMapping("/autocomplete/produtos")
    @ResponseBody
    public List<String> autocompleteProdutos(@RequestParam("query") String query) {
        return produtoService.findByNomeContainingIgnoreCase(query).stream()
                .map(Produto::getNome)
                .collect(Collectors.toList());
    }
    
    // Método para obter o funcionário logado
    private Funcionario getFuncionarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // Ninguém logado ou autenticado anonimamente
        }

        // Dependendo de como você implementou UserDetails para Funcionario
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return funcionarioService.buscaPorNome(username); // Ou findByUsername, findById, etc.
                                                          // Você precisaria de um método no FuncionarioService
                                                          // que encontra o funcionário pelo nome de usuário.
        } else if (principal instanceof String) { // Caso seja um nome de usuário puro (menos comum)
            String username = (String) principal;
            return funcionarioService.buscaPorNome(username);
        }
        return null;
    }
    
    // Altera para concluído
    @GetMapping("/concluido/{id}")
    public String alteraStatusConcluido(@PathVariable UUID id) {
        Entrada entrada = entradaService.buscaPorId(id);
        if (entrada != null) {
            entrada.setConcluido(!entrada.isConcluido());
            if (entrada.isConcluido()) {
                entrada.setDataConcluido(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            } else {
                entrada.setDataConcluido(null);
            }
            entradaService.atualiza(entrada);
        }
        return "redirect:/entradas/lista";
    }
    
    // Mover para saida
    @GetMapping("/moverParaSaida/{id}")
    public String moverParaSaida(@PathVariable UUID id) {
        Entrada entrada = entradaService.buscaPorId(id);
        
        if (entrada != null && entrada.isConcluido()) {
        	Produto produto = entrada.getProduto();
        	
            Saida saida = new Saida();
            saida.setCliente(entrada.getCliente().getNome());
            saida.setProduto(produto.getNome());
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
            saida.setDataSaida(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            saida.setFuncionario(entrada.getFuncionario().getNome());

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
