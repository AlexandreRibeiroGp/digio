package Dto;

import com.teste.digio.Model.Produto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CompraDTO {
    private String codigo;
    private BigDecimal quantidade;
    private String nomeCliente;
    private String cpfCliente;
    private ProdutoDTO produto;
    private BigDecimal valorTotal;

    public CompraDTO(String nomeCliente, String cpfCliente, ProdutoDTO produto, BigDecimal quantidade, BigDecimal valorTotal) {
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }

}
