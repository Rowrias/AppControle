package br.com.appcontrole.domain.saida;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    
    // ---------
    public List<Saida> getDataSaidaDesc() {
        return saidaRepository.findAllByOrderByDataSaidaDesc();
    }

    public Page<Saida> listarPaginado(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return saidaRepository.findAll(pageable);
    }

    public Page<Saida> buscarPorCliente(String cliente, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return saidaRepository.findByClienteContainingIgnoreCase(cliente, pageable);
    }
    
    public Page<Saida> buscarPorProduto(String produto, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return saidaRepository.findByProdutoContainingIgnoreCase(produto, pageable);
    }
}
