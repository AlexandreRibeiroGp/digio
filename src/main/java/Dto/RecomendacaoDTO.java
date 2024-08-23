package Dto;

import lombok.Data;

import java.util.List;

@Data
public class RecomendacaoDTO {
    private String nome;
    private String cpf;
    private List<ProdutoDTO> recomendacoes;

    public RecomendacaoDTO(String nome, String cpf, List<ProdutoDTO> recomendacoes) {
        this.nome = nome;
        this.cpf = cpf;
        this.recomendacoes = recomendacoes;
    }

}
