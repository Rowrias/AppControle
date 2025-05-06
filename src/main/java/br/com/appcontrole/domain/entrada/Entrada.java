package br.com.appcontrole.domain.entrada;

import java.time.LocalDateTime;

import br.com.appcontrole.domain.cliente.Cliente;
import br.com.appcontrole.domain.funcionario.Funcionario;
import br.com.appcontrole.domain.produto.Produto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Produto produto;
	
	@NotNull
	private Integer quantidade;
	
	private Float valorUnitario;
	
	private Float valorTotal;
	
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

	public Cliente getCliente() {
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
	    calcularValorTotal();
	}

	public Float getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Float valorUnitario) {
		if (valorUnitario == null) {
			valorUnitario = (float) 0;
		}
	    this.valorUnitario = valorUnitario;
	    calcularValorTotal();
	}
	
	public Float getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Float valorTotal) {
		this.valorTotal = valorTotal;
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
	
	// calcularValorTotal = setQuantidade X setValorUnitario
	private void calcularValorTotal() {
	    if (this.quantidade != null && this.valorUnitario != null) {
	        this.valorTotal = this.quantidade * this.valorUnitario;
	    }
	}
    
		
}
