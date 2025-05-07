package br.com.appcontrole.domain.saida;

import java.math.BigDecimal;
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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="saida")
public class Saida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Cliente cliente;
    
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Produto produto;
    
    @NotNull
    private Integer quantidade;
    
    @DecimalMin("0.0")
	private BigDecimal valorUnitario = BigDecimal.ZERO;
	
	@DecimalMin("0.0")
	private BigDecimal valorTotal = BigDecimal.ZERO;
    
    private LocalDateTime dataEntrada;
	
	private LocalDateTime dataConcluido;
	
	@NotNull
	private LocalDateTime dataSaida;
	
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Funcionario funcionario;

    // Getters e Setters
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

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }
    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
        calcularValorTotal();
    }
    
    
    ////
    public BigDecimal  getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal  valorTotal) {
		this.valorTotal = valorTotal;
	}
    // Método para calcular valor total
    private void calcularValorTotal() {
        if (this.quantidade != null && this.valorUnitario != null) {
            this.valorTotal = BigDecimal.valueOf(this.quantidade).multiply(this.valorUnitario);
        }
    }
    /////

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

	public LocalDateTime getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(LocalDateTime dataSaida) {
		this.dataSaida = dataSaida;
	}


	public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }


}
