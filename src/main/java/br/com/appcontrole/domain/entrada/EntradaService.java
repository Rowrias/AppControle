package br.com.appcontrole.domain.entrada;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.appcontrole.domain.CRUD;
import br.com.appcontrole.domain.funcionario.Funcionario;
import br.com.appcontrole.domain.funcionario.FuncionarioRepository;

@Service
public class EntradaService implements CRUD<Entrada, Long> {

	@Autowired
	private EntradaRepository entradaRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Override
	public Entrada insere(Entrada entrada) {
		// obtem username do usuário logado
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		// busca o Funcionario correspondente
		Funcionario funcionario = funcionarioRepository.findByUsername(username);
		// associa à entrada
		entrada.setFuncionario(funcionario);
		
		return entradaRepository.save(entrada);
	}

	@Override
	public Entrada atualiza(Entrada entrada) {
		if (entrada.getId() == null || !entradaRepository.existsById(entrada.getId())) {
            throw new IllegalArgumentException("Não encontrado");
        }
        return entradaRepository.save(entrada);
    }

	@Override
	public void remove(Long id) {
		entradaRepository.deleteById(id);
	}

	@Override
	public List<Entrada> buscaTodos() {
        return entradaRepository.findAll();
	}

	@Override
	public Entrada buscaPorId(Long id) {
        return entradaRepository.findById(id).orElse(null);
	}
	
	public List<Entrada> buscaPorStatus(boolean status) {
        return entradaRepository.findByConcluido(status);
    }

	public List<Entrada> getPendentesDataHoraDesc() {
	    return entradaRepository.findByConcluidoFalseOrderByDataEntradaDesc();
	}

	public List<Entrada> getConcluidasDataHoraDesc() {
	    return entradaRepository.findByConcluidoTrueOrderByDataConcluidoDesc();
	}
	
}
