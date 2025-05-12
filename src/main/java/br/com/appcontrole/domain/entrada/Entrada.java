package br.com.appcontrole.domain.entrada;

import java.time.LocalDateTime;

import br.com.appcontrole.domain.cliente.Cliente;
import br.com.appcontrole.domain.funcionario.Funcionario;
import br.com.appcontrole.domain.produto.Produto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
 
@Entity
@Table(name="entrada")
public class Entrada {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Cliente cliente;
	
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Produto produto;
	
	@NotNull
	@Min(0)
	@Column(nullable = false)
	private Integer quantidade;

	@NotNull
    private LocalDateTime dataEntrada;
	
	private LocalDateTime dataConcluido;
	
	@ManyToOne
	private Funcionario funcionario;

    private boolean concluido;

	// Getters Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
