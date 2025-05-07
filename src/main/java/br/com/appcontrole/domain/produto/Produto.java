package br.com.appcontrole.domain.produto;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@Size(max = 255)
	private String descricao;
	
	@Min(0)
	private Integer quantidade = 0;
	
	@DecimalMin("0.0")
	private BigDecimal  valorUnitario;
	
	@DecimalMin("0.0")
	private BigDecimal  valorTotal;
	
	// Getters e Setters
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
		this.quantidade = quantidade;
	    calcularValorTotal();
	}
	
	public BigDecimal  getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(BigDecimal  valorUnitario) {
		if (valorUnitario == null) {
			valorUnitario = BigDecimal.ZERO;
		}
	    this.valorUnitario = valorUnitario;
	    calcularValorTotal();
	}
	
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
	
	@Override
	public String toString() {
	    return this.nome;
	}
	
}
