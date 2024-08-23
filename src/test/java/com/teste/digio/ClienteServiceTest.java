package com.teste.digio;

import Dto.ClienteDTO;
import Dto.RecomendacaoDTO;
import com.teste.digio.Service.ClienteService;
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

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObterClientesFieis() {
        ClienteDTO cliente1 = new ClienteDTO("Cliente 1", "123", new BigDecimal(1000), 5);
        ClienteDTO cliente2 = new ClienteDTO("Cliente 2", "456", new BigDecimal(500), 3);
        List<ClienteDTO> clientesFieis = Arrays.asList(cliente1, cliente2);

        when(clienteService.obterClientesFieis()).thenReturn(clientesFieis);

        List<ClienteDTO> resultado = clienteService.obterClientesFieis();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(clienteService, times(1)).obterClientesFieis();
    }

    @Test
    void testObterRecomendacaoPorCliente() {
        String cpf = "123";
        ClienteDTO cliente = new ClienteDTO("Cliente 1", cpf, new BigDecimal(1000), 5);
        when(clienteService.obterRecomendacaoPorCliente(cpf)).thenReturn(new RecomendacaoDTO(cliente.getNome(), cliente.getCpf(), null));

        RecomendacaoDTO resultado = clienteService.obterRecomendacaoPorCliente(cpf);

        assertNotNull(resultado);
        assertEquals(cpf, resultado.getCpf());
    }
}