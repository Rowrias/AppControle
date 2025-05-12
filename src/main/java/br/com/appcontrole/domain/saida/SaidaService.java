package br.com.appcontrole.domain.saida;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.appcontrole.domain.CRUD;

@Service
public class SaidaService implements CRUD<Saida, UUID> {

    @Autowired
    private SaidaRepository saidaRepository;
    
    @Override
    public Saida insere(Saida saida) {
        return saidaRepository.save(saida);
    }

    @Override
    public Saida atualiza(Saida saida) {
        if (saida.getId() == null || !saidaRepository.existsById(saida.getId())) {
            throw new IllegalArgumentException("Não encontrado");
        }
        return saidaRepository.save(saida);
    }

    @Override
    public void remove(UUID id) {
        saidaRepository.deleteById(id);
    }

    @Override
    public List<Saida> buscaTodos() {
        return saidaRepository.findAll();
    }

    @Override
    public Saida buscaPorId(UUID id) {
        return saidaRepository.findById(id).orElse(null);
    }
    
    public List<Saida> getDataSaidaDesc() {
        return saidaRepository.findAllByOrderByDataSaidaDesc();
    }

	public Page<Saida> listarPaginado(Pageable pageable) {
		return saidaRepository.findAll(pageable);
	}

	public Page<Saida> buscarPorDestino(String destino, Pageable pageable) {
		return saidaRepository.findByDestinoContainingIgnoreCase(destino, pageable);
	}
}
