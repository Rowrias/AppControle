package br.com.appcontrole.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.appcontrole.model.Cliente;
import br.com.appcontrole.repository.ClienteRepository;

@Service
public class ClienteService implements CRUD<Cliente, Long> {
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public Cliente insere(Cliente cliente) {
        return clienteRepository.save(cliente);
	}

	@Override
    public Cliente atualiza(Cliente updatedCliente) {
        if (updatedCliente.getId() == null || !clienteRepository.existsById(updatedCliente.getId())) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }

        Cliente existingCliente = clienteRepository.findById(updatedCliente.getId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        // Atualizar campos do existingCliente com campos do updatedCliente
        existingCliente.setNome(updatedCliente.getNome());
        existingCliente.setCnpj(updatedCliente.getCnpj());
        existingCliente.setCpf(updatedCliente.getCpf());
        existingCliente.setEmail(updatedCliente.getEmail());
        existingCliente.setCelular(updatedCliente.getCelular());
        existingCliente.setTelefone(updatedCliente.getTelefone());

        // Atualizar a coleção de entradas
        existingCliente.updateEntradas(updatedCliente.getEntradas());

        // Persistir as alterações
        return clienteRepository.save(existingCliente);
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
	
	public Cliente clienteExiste(String nome) {
		Cliente cliente = clienteRepository.findByNome(nome);
        if (cliente == null) {
            cliente = new Cliente();
            cliente.setNome(nome);
            clienteRepository.save(cliente);
        }
        return cliente;
    }

}
