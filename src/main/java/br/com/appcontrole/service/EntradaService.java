package br.com.appcontrole.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.appcontrole.model.Entrada;
import br.com.appcontrole.repository.EntradaRepository;

@Service
public class EntradaService implements CRUD<Entrada, Long> {

	@Autowired
	private EntradaRepository entradaRepository;
	
	@Override
	public Entrada insere(Entrada entrada) {
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
		// TODO Auto-generated method stub
        return entradaRepository.findById(id).orElse(null);
	}

}
