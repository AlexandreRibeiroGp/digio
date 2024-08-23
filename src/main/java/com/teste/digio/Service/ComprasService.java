package com.teste.digio.Service;

import Dto.ClienteDTO;
import Dto.CompraDTO;
import Dto.ProdutoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComprasService {
    private final RestTemplate restTemplate;

    @Autowired
    public ComprasService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<CompraDTO> listarComprasOrdenadasPorValor() {
        String urlCompras = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json";
        String urlProdutos = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json";

        ClienteDTO[] clientes = restTemplate.getForObject(urlCompras, ClienteDTO[].class);
        ProdutoDTO[] produtos = restTemplate.getForObject(urlProdutos, ProdutoDTO[].class);

        List<CompraDTO> compras = new ArrayList<>();

        for (ClienteDTO cliente : clientes) {
            for (CompraDTO compra : cliente.getCompras()) {
                ProdutoDTO produto = buscarProdutoPorCodigo(produtos, compra.getCodigo());
                if (produto != null) {
                    BigDecimal valorTotal = produto.getPreco().multiply(compra.getQuantidade());
                    compras.add(new CompraDTO(cliente.getNome(), cliente.getCpf(), produto, compra.getQuantidade(), valorTotal));
                }
            }
        }

        return compras.stream()
                .sorted(Comparator.comparing(CompraDTO::getValorTotal))
                .collect(Collectors.toList());
    }

    public CompraDTO obterMaiorCompraPorAno(Long ano) {
        String urlCompras = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json";
        String urlProdutos = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json";

        ClienteDTO[] clientes = restTemplate.getForObject(urlCompras, ClienteDTO[].class);
        ProdutoDTO[] produtos = restTemplate.getForObject(urlProdutos, ProdutoDTO[].class);

        return Arrays.stream(clientes)
                .flatMap(cliente -> cliente.getCompras().stream()
                        .map(compra -> {
                            ProdutoDTO produto = buscarProdutoPorCodigo(produtos, compra.getCodigo());
                            if (produto != null && Objects.equals(produto.getAno_compra(), ano)) {
                                BigDecimal valorTotal = produto.getPreco().multiply(compra.getQuantidade());
                                return new CompraDTO(cliente.getNome(), cliente.getCpf(), produto, compra.getQuantidade(), valorTotal);
                            }
                            return null;
                        })
                        .filter(Objects::nonNull))
                .max(Comparator.comparing(CompraDTO::getValorTotal))
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma compra encontrada para o ano: " + ano));
    }

    private ProdutoDTO buscarProdutoPorCodigo(ProdutoDTO[] produtos, String codigo)
    {
        return Arrays.stream(produtos)
                .filter(produto -> produto.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }
}