package Dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ClienteDTO {
    private String nome;
    private String cpf;
    private BigDecimal totalGasto;
    private int numeroCompras;
    private List<CompraDTO> compras;

    public ClienteDTO(String nome, String cpf, BigDecimal totalGasto, int numeroCompras) {
        this.nome = nome;
        this.cpf = cpf;
        this.totalGasto = totalGasto;
        this.numeroCompras = numeroCompras;
    }
}
