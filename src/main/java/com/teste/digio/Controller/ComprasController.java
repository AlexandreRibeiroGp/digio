package com.teste.digio.Controller;

import Dto.CompraDTO;
import com.teste.digio.Service.ComprasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/compras")
public class ComprasController {

    private final ComprasService compraService;

    @Autowired
    public ComprasController(ComprasService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public ResponseEntity<List<CompraDTO>> listarComprasOrdenadasPorValor() {
        List<CompraDTO> compras = compraService.listarComprasOrdenadasPorValor();
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/maior-compra/{ano}")
    public ResponseEntity<?> obterMaiorCompraPorAno(@PathVariable Long ano) {
        try {
            CompraDTO maiorCompra = compraService.obterMaiorCompraPorAno(ano);
            return ResponseEntity.ok(maiorCompra);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Nenhuma compra encontrada para o ano: " + ano, HttpStatus.NOT_FOUND);
        }
    }

}
