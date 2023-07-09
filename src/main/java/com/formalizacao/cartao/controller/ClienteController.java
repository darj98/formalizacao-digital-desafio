package com.formalizacao.cartao.controller;

import com.formalizacao.cartao.model.Cliente;
import com.formalizacao.cartao.service.ClienteService;
import com.formalizacao.cartao.util.Messages;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.getAllClientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        Cliente cliente = clienteService.getClienteById(id).getBody();
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente) {
        if (StringUtils.isEmpty(cliente.getNome())) {
            return ResponseEntity.badRequest().body(Messages.obterMensagemNomeCliente());
        }
        if (StringUtils.isEmpty(cliente.getCpf())) {
            return ResponseEntity.badRequest().body(Messages.obterMensagemCpfCliente());
        }

        Cliente createdCliente = clienteService.createCliente(cliente);
        return ResponseEntity.ok(createdCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente updatedCliente = clienteService.updateCliente(id, cliente).getBody();
        if (updatedCliente != null) {
            return ResponseEntity.ok(updatedCliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCliente(@PathVariable Long id) {
        boolean clienteDeleted = clienteService.deleteCliente(id);
        if (clienteDeleted) {
            return ResponseEntity.ok(Messages.obterMensagemClienteDeletado());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{cpf}/ultimaSimulacao")
    public ResponseEntity<String> getUltimaSimulacao(@PathVariable String cpf) {
        Cliente cliente = clienteService.getClienteByCpf(cpf).getBody();
        if (cliente != null && cliente.getUltimaSimulacao() != null) {
            return ResponseEntity.ok(Messages.obterMensagemUltimaSimulacao(cpf,cliente.getUltimaSimulacao()));
        } else if(cliente != null && cliente.getUltimaSimulacao() == null){
            return ResponseEntity.ok(Messages.obterMensagemUltimaSimulacaoInexistente());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
