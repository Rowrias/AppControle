package br.com.appcontrole.domain.saida;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="saida")
public class Saida {
	
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;
    
	@NotNull(message = "O nome do cliente é obrigatório.") // Mensagem de erro personalizada
    @NotBlank(message = "O nome do cliente não pode estar vazio.") // Melhor que @NotNull para Strings
    @Size(min = 3, max = 50, message = "O nome do cliente deve ter entre 3 e 50 caracteres.")
    private String cliente;
    
	@NotNull(message = "O nome do produto é obrigatório.")
    @NotBlank(message = "O nome do produto não pode estar vazio.")
    @Size(min = 3, max = 50, message = "O nome do produto deve ter entre 3 e 50 caracteres.")
    private String produto;
	
	@NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 0, message = "A quantidade deve ser no mínimo {value}.")
    private Integer quantidade;
    
	@DecimalMin(value = "0.0", message = "O valor total deve ser no mínimo {value}.")
	private BigDecimal valorUnitario = BigDecimal.ZERO;
	
	@DecimalMin(value = "0.0", message = "O valor total deve ser no mínimo {value}.")
	private BigDecimal valorTotal = BigDecimal.ZERO;
    
	@NotNull(message = "A data de entrada é obrigatória.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataEntrada;
	
	@NotNull(message = "A data de conclusão é obrigatória.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataConcluido;

	@NotNull(message = "A data de saída é obrigatória.")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dataSaida;
	
	@NotNull(message = "O nome do funcionário é obrigatório.")
    @NotBlank(message = "O nome do funcionário não pode estar vazio.")
	private String  funcionario;
    
	// Construtor padrão (necessário para JPA)
    public Saida() {
    }
	
    // Getters e Setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
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
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }
    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
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
    	// Garante que quantidade e valorUnitario não são nulos antes do cálculo
        if (this.quantidade != null && this.valorUnitario != null) {
            this.valorTotal = new BigDecimal(this.quantidade).multiply(this.valorUnitario);
        } else {
            // Se um dos campos for nulo, o valor total também pode ser zero ou nulo, dependendo da sua regra.
            // Definindo como ZERO se um dos componentes for nulo, para evitar NullPointer.
            this.valorTotal = BigDecimal.ZERO;
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
	
	public String getFuncionario() {
        return funcionario;
    }
    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }
    
 // --- Métodos de Ciclo de Vida para Calcular Valor Total Automaticamente ---
    // Isso garante que valorTotal seja sempre calculado antes de persistir ou atualizar.
    @PrePersist
    @PreUpdate
    private void preProcess() {
        calcularValorTotal();
    }
	
}
