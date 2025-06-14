package br.com.appcontrole.domain.produto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="produto")
public class Produto {
	
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;
	
	@Column(unique = true)
	@Size(min = 3, max = 50, message = "- O nome do produto deve ter entre {min} e {max} caracteres.")
	@NotBlank(message = "- O nome do produto é obrigatório.")
	private String nome;
	
	@Size(max = 100, message = "- A descrição não pode ter mais de {max} caracteres.")
	private String descricao;
	
	@Column(nullable = false)
	@Min(value = 0, message = "- A quantidade deve ser no mínimo {value}.")
	private Integer quantidade = 0;
	
	@DecimalMin(value = "0.0", message = "- O valor total deve ser no mínimo {value}.")
	private BigDecimal  valorUnitario = BigDecimal.ZERO;
	
	@DecimalMin(value = "0.0", message = "- O valor total deve ser no mínimo {value}.")
	private BigDecimal  valorTotal = BigDecimal.ZERO;
	
	// Getters e Setters
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
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getQuantidade() {
		return quantidade;
	}
	public void setQuantidade(Integer quantidade) {
		if (quantidade == null) {
			quantidade = 0;
		}
		this.quantidade = quantidade;
	    calcularValorTotal();
	}
	
	public BigDecimal  getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(BigDecimal  valorUnitario) {
		if (valorUnitario == null) {
            this.valorUnitario = BigDecimal.ZERO;
        } else {
            this.valorUnitario = valorUnitario;
        }
        calcularValorTotal();
	}
	
	
	//////
	public BigDecimal  getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal  valorTotal) {
		this.valorTotal = valorTotal;
	}

	// calcularValorTotal = setEstoque X setValorUnitario
	private void calcularValorTotal() {
	    if (this.quantidade != null && this.valorUnitario != null) {
	        this.valorTotal = BigDecimal.valueOf(this.quantidade).multiply(this.valorUnitario);
	    }
	}
	//////
	
	@Override
	public String toString() {
	    return this.nome;
	}
	
}
