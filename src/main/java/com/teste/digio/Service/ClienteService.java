package com.teste.digio.Service;

import Dto.ClienteDTO;
import Dto.ProdutoDTO;
import Dto.RecomendacaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final RestTemplate restTemplate;

    @Autowired
    public ClienteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ClienteDTO> obterClientesFieis() {
        String urlCompras = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json";
        String urlProdutos = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json";

        ClienteDTO[] clientes = restTemplate.getForObject(urlCompras, ClienteDTO[].class);
        ProdutoDTO[] produtos = restTemplate.getForObject(urlProdutos, ProdutoDTO[].class);

        if (clientes == null || produtos == null) {
            throw new IllegalStateException("Não foi possível obter dados de clientes ou produtos.");
        }

        return Arrays.stream(clientes)
                .map(cliente -> {
                    BigDecimal totalGasto = cliente.getCompras().stream()
                            .map(compra -> {
                                ProdutoDTO produto = buscarProdutoPorCodigo(produtos, compra.getCodigo());
                                return produto != null ? produto.getPreco().multiply(compra.getQuantidade()) : BigDecimal.ZERO;
                            })
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new ClienteDTO(cliente.getNome(), cliente.getCpf(), totalGasto, cliente.getCompras().size());
                })
                .sorted(Comparator.comparing(ClienteDTO::getTotalGasto).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public RecomendacaoDTO obterRecomendacaoPorCliente(String cpf) {
        String urlCompras = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json";
        String urlProdutos = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json";

        ClienteDTO[] clientes = restTemplate.getForObject(urlCompras, ClienteDTO[].class);
        ProdutoDTO[] produtos = restTemplate.getForObject(urlProdutos, ProdutoDTO[].class);

        if (clientes == null || produtos == null) {
            throw new IllegalArgumentException("Clientes ou produtos não encontrados");
        }

        ClienteDTO cliente = Arrays.stream(clientes)
                .filter(c -> c.getCpf().equals(cpf))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com CPF: " + cpf));

        Map<String, Long> tipoVinhoCount = cliente.getCompras().stream()
                .map(compra -> {

                    System.out.println("Compra: " + compra);

                    ProdutoDTO produto = buscarProdutoPorCodigo(produtos, compra.getCodigo());
                    System.out.println("Produto encontrado: " + produto);
                    return produto != null ? produto.getTipo_vinho() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(tipo -> tipo, Collectors.counting()));
        System.out.println("Contagem de tipos de vinho: " + tipoVinhoCount);
        String tipoVinhoMaisComprado = tipoVinhoCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new NoSuchElementException("Nenhum tipo de vinho encontrado"));

        // Recomendação de vinhos do tipo mais comprado
        List<ProdutoDTO> recomendacoes = Arrays.stream(produtos)
                .filter(produto -> produto.getTipo_vinho().equals(tipoVinhoMaisComprado))
                .collect(Collectors.toList());

        return new RecomendacaoDTO(cliente.getNome(), cliente.getCpf(), recomendacoes);
    }

    private ProdutoDTO buscarProdutoPorCodigo(ProdutoDTO[] produtos, String codigo) {
        return Arrays.stream(produtos)
                .filter(produto -> produto.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }
}