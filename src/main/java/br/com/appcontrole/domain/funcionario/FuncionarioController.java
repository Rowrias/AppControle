package br.com.appcontrole.domain.funcionario;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/funcionarios")
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	// Insere
	@PostMapping("/lista")
    public String novoFuncionario(@Valid @ModelAttribute("funcionario") Funcionario funcionario,
						            BindingResult result,
						            RedirectAttributes attr) {
		
		// Verifica erros de validação primeiro
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.funcionario", result);
            attr.addFlashAttribute("funcionario", funcionario); // Para preservar os dados
            attr.addFlashAttribute("erro", "Verifique os dados do funcionário e tente novamente.");
            attr.addFlashAttribute("roles", Role.values()); // Passe as roles novamente em caso de erro
            return "redirect:/funcionarios/lista";
        }
        
        // Lógica para verificar nome ou username duplicado
        Funcionario funcionarioExistenteNome = funcionarioService.buscaPorNome(funcionario.getNome());
        Funcionario funcionarioExistenteUsername = funcionarioService.buscaPorUsuario(funcionario.getUsername());
        
        if (funcionarioExistenteNome != null) {
            attr.addFlashAttribute("erro", "Funcionário com este nome já existe.");
            attr.addFlashAttribute("funcionario", funcionario);
            attr.addFlashAttribute("roles", Role.values());
            return "redirect:/funcionarios/lista";
        }
        
        if (funcionarioExistenteUsername != null) {
            attr.addFlashAttribute("erro", "Funcionário com este usuário já existe.");
            attr.addFlashAttribute("funcionario", funcionario);
            attr.addFlashAttribute("roles", Role.values());
            return "redirect:/funcionarios/lista";
        }
        
        // Criptografar a senha antes de salvar (exemplo, você deve usar um PasswordEncoder real)
        // Se você não está usando Spring Security PasswordEncoder, você precisará implementar algo simples
        // Ex: funcionario.setPassword(new BCryptPasswordEncoder().encode(funcionario.getPassword()));
        // Por agora, apenas certifique-se de que a senha não seja nula
        if (funcionario.getPassword() == null || funcionario.getPassword().isEmpty()) {
            // Isso não deve acontecer com @NotBlank, mas como fallback
            attr.addFlashAttribute("erro", "A senha não pode ser vazia.");
            attr.addFlashAttribute("funcionario", funcionario);
            attr.addFlashAttribute("roles", Role.values());
            return "redirect:/funcionarios/lista";
        }
        
		funcionarioService.insere(funcionario);
		attr.addFlashAttribute("mensagem", "Funcionário adicionado com sucesso.");
        return "redirect:/funcionarios/lista";
    }
	
	// Lista com ordenação
    @GetMapping("/lista")
    public String listaFuncionario(Model model,
                       @RequestParam(name = "sortBy", required = false, defaultValue = "nome") String sortBy,
                       @RequestParam(name = "sortDirection", required = false, defaultValue = "asc") String sortDirection) {

        Sort sort = Sort.by(sortBy);
        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }

        List<Funcionario> funcionarios = funcionarioService.buscaTodosOrdenado(sort);
        model.addAttribute("funcionarios", funcionarios);
        model.addAttribute("roles", Role.values());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDirection", sortDirection);
        
        // Adiciona um novo objeto Funcionario ao modelo, se não estiver presente (para o formulário de adição)
        if (!model.containsAttribute("funcionario")) {
            model.addAttribute("funcionario", new Funcionario());
        }
        return "funcionarios/lista";
    }
	
	// Editar
	@GetMapping("/editar/{id}")
    public String editarEntrada(@PathVariable UUID id, Model model) {
		
		if (!model.containsAttribute("funcionario")) {
			Funcionario funcionario = funcionarioService.buscaPorId(id);
			model.addAttribute("funcionario", funcionario);			
		}
        model.addAttribute("roles", Role.values());

        return "funcionarios/editar";
    }

    @PostMapping("/editar/{id}")
    public String atualizarEntrada(@PathVariable UUID id, 
    							@Valid @ModelAttribute("funcionario") Funcionario funcionario,
    							BindingResult result,
    							RedirectAttributes attr) {
    	// Verifica erros de validação
        // Note que a validação de senha pode ser um problema aqui se o campo estiver vazio.
        // Se a senha for opcional na edição (ou seja, se o usuário não digitar nada, mantém a antiga),
        // você precisará de uma lógica para ignorar a validação @NotBlank/@Size para o campo 'password'
        // SOMENTE se ele estiver vazio.
        // Uma forma é remover o @NotBlank/@Size da entidade e fazer a validação manual aqui,
        // ou usar grupos de validação, ou um DTO para edição.
    	String oldPassword = funcionarioService.buscaPorId(id).getPassword();
    	
    	// Se a senha no formulário estiver vazia, não valide o campo password e use a senha antiga.
        // Isso só funciona se você permitir que a senha NÃO seja alterada na edição.
        if (funcionario.getPassword() == null || funcionario.getPassword().isEmpty()) {
            funcionario.setPassword(oldPassword); // Mantém a senha antiga
            // Remove erros de validação para o campo 'password' se ele foi validado como NotBlank
            result.rejectValue("password", "", ""); // Remove o erro se houver
        }
        
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.funcionario", result);
            attr.addFlashAttribute("funcionario", funcionario);
            attr.addFlashAttribute("erro", "Verifique os dados do funcionário e tente novamente.");
            attr.addFlashAttribute("roles", Role.values());
            return "redirect:/funcionarios/editar/" + id;
        }
        
        // Lógica para verificar nome ou username duplicado, excluindo o funcionário atual
        Funcionario funcionarioExistenteNome = funcionarioService.buscaPorNome(funcionario.getNome());
        if (funcionarioExistenteNome != null && !funcionarioExistenteNome.getId().equals(id)) {
            attr.addFlashAttribute("erro", "Já existe outro funcionário com este nome.");
            attr.addFlashAttribute("funcionario", funcionario);
            attr.addFlashAttribute("roles", Role.values());
            return "redirect:/funcionarios/editar/" + id;
        }

        Funcionario funcionarioExistenteUsername = funcionarioService.buscaPorUsuario(funcionario.getUsername());
        if (funcionarioExistenteUsername != null && !funcionarioExistenteUsername.getId().equals(id)) {
            attr.addFlashAttribute("erro", "Já existe outro funcionário com este usuário.");
            attr.addFlashAttribute("funcionario", funcionario);
            attr.addFlashAttribute("roles", Role.values());
            return "redirect:/funcionarios/editar/" + id;
        }
        funcionario.setId(id);
        // Se a senha foi preenchida no formulário, você deve criptografá-la antes de salvar
        // Se não foi, ela já foi setada para a senha antiga acima.
        funcionarioService.atualiza(funcionario);
        attr.addFlashAttribute("mensagem", "Funcionário atualizado com sucesso.");
        return "redirect:/funcionarios/lista";
    }
        
    // Remover
    @GetMapping("/remover/{id}")
    public String removerFuncionario(@PathVariable UUID id, RedirectAttributes attr) {
    	try {
    		funcionarioService.remove(id);
    		attr.addFlashAttribute("mensagem", "Funcionário removido com sucesso.");
    	} catch (DataIntegrityViolationException e) {
    		attr.addFlashAttribute("erro", "Não é possível excluir o funcionário devido a registros dependentes.");
    	}
    	return "redirect:/funcionarios/lista";
    }
    
}
