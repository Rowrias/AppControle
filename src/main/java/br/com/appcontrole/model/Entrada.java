package br.com.appcontrole.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="entrada")
public class Entrada {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
    @ManyToOne
    private Cliente cliente;
	
	@NotNull
	@Size(min = 1, max = 50)
	private String produto;
	
	@NotNull
	private Integer quantidade;
	
	private Float valorUnitario;
	
	private Float valorTotal;
	
	@NotNull
    private LocalDateTime dataEntrada;
	
	private LocalDateTime dataConcluido;

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

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
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
