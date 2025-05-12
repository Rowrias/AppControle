package br.com.appcontrole.domain.cliente;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.appcontrole.domain.CRUD;
import br.com.appcontrole.domain.entrada.Entrada;
import jakarta.transaction.Transactional;

@Service
public class ClienteService implements CRUD<Cliente, UUID> {
	
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

	@Transactional
	@Override
	public void remove(UUID id) {
		clienteRepository.deleteById(id);
	}

	@Override
	public List<Cliente> buscaTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscaPorId(UUID id) {
		return clienteRepository.findById(id).orElse(null);
	}
	
	//
	public List<Cliente> buscaTodosOrdenadoPorNome() {
        return clienteRepository.findAllByOrderByNomeAsc();
    }
	
	public Cliente buscaPorNome(String nome) {
		return clienteRepository.findByNome(nome);
    }

	public Cliente findByNomeIgnoreCase(String nomeCliente) {
		return clienteRepository.findByNomeIgnoreCase(nomeCliente)
		        .orElseGet(() -> {
		            Cliente novo = new Cliente();
		            novo.setNome(nomeCliente);
		            return clienteRepository.save(novo);
		        });
	}

	public Collection<Entrada> buscaPorNomeParcial(String nomeParcial) {
		return clienteRepository.findByNomeContainingIgnoreCase(nomeParcial);
	}
	
}
