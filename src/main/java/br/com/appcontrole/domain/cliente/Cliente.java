package br.com.appcontrole.domain.cliente;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="cliente")
public class Cliente {
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;
	 
	@NotNull
	// @Size(min = 3, max = 50)
	@Column(unique = true)
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
	
	//Getters Setters
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if (nome != null) {
            this.nome = nome.toLowerCase();
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
	
	@Override
    public String toString() {
        return nome;
    }
}
