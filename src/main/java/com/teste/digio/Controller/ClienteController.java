package com.teste.digio.Controller;

import Dto.ClienteDTO;
import Dto.RecomendacaoDTO;
import com.teste.digio.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/fieis")
    public ResponseEntity<List<ClienteDTO>> obterClientesFieis() {
        try {
            List<ClienteDTO> clientesFieis = clienteService.obterClientesFieis();
            return new ResponseEntity<>(clientesFieis, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/recomendacao")
    public ResponseEntity<RecomendacaoDTO> obterRecomendacaoPorCliente(@RequestParam String cpf) {
        try {
            RecomendacaoDTO recomendacao = clienteService.obterRecomendacaoPorCliente(cpf);
            return new ResponseEntity<>(recomendacao, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}