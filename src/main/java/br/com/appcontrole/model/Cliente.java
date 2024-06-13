package br.com.appcontrole.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="cliente")
public class Cliente {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 1, max = 50)
	private String nome;
	
	@Column(length = 18)
	private String cnpj;
	
	@Column(length = 14)
	private String cpf;
	
	@Column(length = 50)
	@Email
	private String email;
	
	@Column(length = 15)
	private String celular;
	
	@Column(length = 14)
	private String telefone;
	
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Entrada> entradas = new ArrayList<>();

	// Getters Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if (nome != null) {
            this.nome = nome.toLowerCase();
        } else {
            this.nome = null;
        }
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public List<Entrada> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
    }

    public void addEntrada(Entrada entrada) {
        entradas.add(entrada);
        entrada.setCliente(this);
    }
    
    public void updateEntradas(List<Entrada> newEntradas) {
        for (Entrada newEntrada : newEntradas) {
            boolean updated = false;
            for (Entrada existingEntrada : this.entradas) {
                if (existingEntrada.getId().equals(newEntrada.getId())) {
                    existingEntrada.setProduto(newEntrada.getProduto());
                    existingEntrada.setQuantidade(newEntrada.getQuantidade());
                    existingEntrada.setValorUnitario(newEntrada.getValorUnitario());
                    existingEntrada.setValorTotal(newEntrada.getValorTotal());
                    existingEntrada.setDataEntrada(newEntrada.getDataEntrada());
                    existingEntrada.setDataConcluido(newEntrada.getDataConcluido());
                    existingEntrada.setConcluido(newEntrada.isConcluido());
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                this.addEntrada(newEntrada);
            }
        }
    }
	
	@Override
    public String toString() {
        return nome;
    }
}
