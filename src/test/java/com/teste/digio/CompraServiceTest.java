package com.teste.digio;

import Dto.CompraDTO;
import com.teste.digio.Service.ComprasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ComprasServiceTest {

    @InjectMocks
    private ComprasService comprasService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarComprasOrdenadasPorValor() {
        CompraDTO compra1 = new CompraDTO("Cliente 1", "123", null, new BigDecimal(5), BigDecimal.valueOf(500));
        CompraDTO compra2 = new CompraDTO("Cliente 2", "456", null, new BigDecimal(3), BigDecimal.valueOf(300));
        List<CompraDTO> compras = Arrays.asList(compra1, compra2);

        when(comprasService.listarComprasOrdenadasPorValor()).thenReturn(compras);

        List<CompraDTO> resultado = comprasService.listarComprasOrdenadasPorValor();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(comprasService, times(1)).listarComprasOrdenadasPorValor();
    }

    @Test
    void testObterMaiorCompraPorAno() {
        String ano = "2023";
        CompraDTO compra = new CompraDTO("Cliente 1", "123", null, new BigDecimal(5), BigDecimal.valueOf(500));
        when(comprasService.obterMaiorCompraPorAno(anyLong())).thenReturn(compra);

        CompraDTO resultado = comprasService.obterMaiorCompraPorAno(Long.valueOf(ano));

        assertNotNull(resultado);
        assertEquals("123", resultado.getCpfCliente());
    }
}