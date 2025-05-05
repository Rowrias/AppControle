package br.com.appcontrole.domain.saida;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.appcontrole.domain.CRUD;

@Service
public class SaidaService implements CRUD<Saida, Long> {

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
    public void remove(Long id) {
        saidaRepository.deleteById(id);
    }

    @Override
    public List<Saida> buscaTodos() {
        return saidaRepository.findAll();
    }

    @Override
    public Saida buscaPorId(Long id) {
        return saidaRepository.findById(id).orElse(null);
    }
    
    public List<Saida> getDataSaidaDesc() {
        return saidaRepository.findAllByOrderByDataSaidaDesc();
    }
}
