package br.com.appcontrole.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.appcontrole.model.Funcionario;
import br.com.appcontrole.repository.FuncionarioRepository;

@Service
public class FuncionarioService implements CRUD<Funcionario, Long>{

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Override
	public Funcionario insere(Funcionario funcionario) {
		// Criptografar a senha antes de salvar
        String encodedPassword = passwordEncoder.encode(funcionario.getPassword());
        funcionario.setPassword(encodedPassword);
        
        return funcionarioRepository.save(funcionario);
	}

	@Override
	public Funcionario atualiza(Funcionario funcionario) {
		if (funcionario.getId() == null || !funcionarioRepository.existsById(funcionario.getId())) {
            throw new IllegalArgumentException("Não encontrado");
        }
        return funcionarioRepository.save(funcionario);
	}

	@Override
	public void remove(Long id) {
		funcionarioRepository.deleteById(id);
	}

	@Override
	public List<Funcionario> buscaTodos() {
		return funcionarioRepository.findAll();
	}
	
	@Override
	public Funcionario buscaPorId(Long id) {
		return funcionarioRepository.findById(id).orElse(null);
	}
	
	public List<Funcionario> buscaTodosOrdenadoPorNome() {
        return funcionarioRepository.findAllByOrderByNomeAsc();
    }
	
	@Autowired
    private PasswordEncoder passwordEncoder;

    // Criptografa a senha antes de salvar
    public void inserirUsuario(Funcionario funcionario) {
        String encodedPassword = passwordEncoder.encode(funcionario.getPassword());
        funcionario.setPassword(encodedPassword);
        funcionarioRepository.save(funcionario);
    }

}
