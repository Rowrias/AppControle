package br.com.appcontrole.domain.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.appcontrole.domain.CRUD;

@Service
public class ClienteService implements CRUD<Cliente, Long> {
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public Cliente insere(Cliente cliente) {
        return clienteRepository.save(cliente);
	}

	@Override
    public Cliente atualiza(Cliente cliente) {
		if (cliente.getId() == null || !clienteRepository.existsById(cliente.getId())) {
            throw new IllegalArgumentException("Não encontrado");
        }
        // Persistir as alterações
        return clienteRepository.save(cliente);
    }

	@Override
	public void remove(Long id) {
		clienteRepository.deleteById(id);
	}

	@Override
	public List<Cliente> buscaTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscaPorId(Long id) {
		return clienteRepository.findById(id).orElse(null);
	}
	
	public List<Cliente> buscaTodosOrdenadoPorNome() {
        return clienteRepository.findAllByOrderByNomeAsc();
    }
	
	public Cliente buscaPorNome(String nome) {
		return clienteRepository.findByNome(nome);
    }

}
