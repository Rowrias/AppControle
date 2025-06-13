package br.com.appcontrole.domain.funcionario;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="funcionario")
public class Funcionario {

	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;
	
	@NotNull(message = "O nome é obrigatório.")
	@Size(min = 3, max = 50, message = "O nome deve ter entre {min} e {max} caracteres.")
	@Column(unique = true)
	private String nome;
	
	@Column(length = 14)
	private String cpf;
	
	@Column(length = 50)
	@Email(message = "E-mail inválido")
	private String email;
	
	@Column(length = 15)
	private String celular;
	
	@Column(length = 14)
	private String telefone;

	@NotBlank(message = "O nome de usuário é obrigatório.") // Certifica que não é nulo nem vazio
	@Size(min = 4, max = 14, message = "O nome de usuário deve ter entre {min} e {max} caracteres.")
	@Column(unique = true) // Assumindo que o username deve ser único
	private String username;
	
	@NotBlank(message = "A senha é obrigatória.")
	@Size(min = 4, max = 60, message = "A senha deve ter entre {min} e {max} caracteres.")
    private String password;
    
	@NotNull(message = "O nível de acesso é obrigatório.")
    @Enumerated(EnumType.STRING)
    private Role role;
    
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
        } else {
            this.nome = null;
        }
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

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
}
