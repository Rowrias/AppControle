package br.com.appcontrole.domain.entrada;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.appcontrole.domain.CRUD;

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
