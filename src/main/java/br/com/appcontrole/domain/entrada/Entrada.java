package br.com.appcontrole.domain.entrada;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.appcontrole.domain.cliente.Cliente;
import br.com.appcontrole.domain.funcionario.Funcionario;
import br.com.appcontrole.domain.produto.Produto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
 
@Entity
@Table(name="entrada")
public class Entrada {
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;
	
	@NotNull(message = "- O cliente é obrigatório.")
	@Valid // Valida o objeto Cliente aninhado
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Cliente cliente;
	
	@NotNull(message = "- O produto é obrigatório.")
	@Valid // Valida o objeto Cliente aninhado
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Produto produto;
	
	@Min(value = 0, message = "- A quantidade deve ser no mínimo {value}.")
	@NotNull(message = "- A quantidade é obrigatória.")
	private Integer quantidade;

	@NotNull(message = "- A data de entrada é obrigatória.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataEntrada;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataConcluido;

	@ManyToOne
	private Funcionario funcionario;

    private boolean concluido;

	// Getters Setters
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

	public @NotNull Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public LocalDateTime getDataEntrada() {
		return dataEntrada;
	}
	public void setDataEntrada(LocalDateTime dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public LocalDateTime getDataConcluido() {
		return dataConcluido;
	}
	public void setDataConcluido(LocalDateTime dataConcluido) {
		this.dataConcluido = dataConcluido;
	}
	
	public Funcionario getFuncionario() {
        return funcionario;
    }
    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

	public boolean isConcluido() {
		return concluido;
	}
	public void setConcluido(boolean concluido) {
		this.concluido = concluido;
	}
	
}
