package Dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProdutoDTO {
    private String codigo;
    private String tipo_vinho;
    private BigDecimal preco;
    private String safra;
    private Long ano_compra;
}
