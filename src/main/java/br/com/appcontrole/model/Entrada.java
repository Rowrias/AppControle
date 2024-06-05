package br.com.appcontrole.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="entrada")
public class Entrada {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 1, max = 255)
	private String cliente;
	
	@NotNull
	@Size(min = 1, max = 255)
	private String produto;
	
	@NotNull
	@Positive
	private Integer quantidade;
	
	@Positive
	private Float valorUnitario;
	
	private Float valorTotal;
	
    private LocalDateTime dataHora;

    // Getters Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
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
	    this.valorUnitario = valorUnitario;
	    calcularValorTotal();
	}
	
	public Float getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Float valorTotal) {
		this.valorTotal = valorTotal;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}
	
	// calcularValorTotal = setQuantidade X setValorUnitario
		private void calcularValorTotal() {
		    if (this.quantidade != null && this.valorUnitario != null) {
		        this.valorTotal = this.quantidade * this.valorUnitario;
		    }
		}
    
}
