package br.com.appcontrole.domain.funcionario;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.appcontrole.domain.CRUD;
import jakarta.transaction.Transactional;

@Service
public class FuncionarioService implements CRUD<Funcionario, UUID>{

	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public Funcionario insere(Funcionario funcionario) {
		// Criptografar a senha antes de salvar
        String encodedPassword = passwordEncoder.encode(funcionario.getPassword());
        funcionario.setPassword(encodedPassword);
        
        return funcionarioRepository.save(funcionario);
	}

	@Override
	@Transactional
	public Funcionario atualiza(Funcionario funcionario) {
		if (funcionario.getId() == null || !funcionarioRepository.existsById(funcionario.getId())) {
            throw new IllegalArgumentException("Não encontrado");
        }
		String encodedPassword = passwordEncoder.encode(funcionario.getPassword());
        funcionario.setPassword(encodedPassword);
        
        return funcionarioRepository.save(funcionario);
	}

	@Override
	@Transactional
	public void remove(UUID id) {
		funcionarioRepository.deleteById(id);
	}

	@Override
	public List<Funcionario> buscaTodos() {
		return funcionarioRepository.findAll();
	}
	
	@Override
	public Funcionario buscaPorId(UUID id) {
		return funcionarioRepository.findById(id).orElse(null);
	}
	
	// -----------
	public List<Funcionario> buscaTodosOrdenado(Sort sort) {
        return funcionarioRepository.findAll(sort);
    }

	public Funcionario buscaPorUsuario(String username) {
	    return funcionarioRepository.findByUsername(username);
	}

	
}
