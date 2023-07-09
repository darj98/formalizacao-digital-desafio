package com.formalizacao.cartao.service;

import com.formalizacao.cartao.model.Cliente;
import com.formalizacao.cartao.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    public ResponseEntity<Cliente> getClienteById(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Cliente> getClienteByCpf(String cpf) {
        Optional<Cliente> clienteOptional = Optional.ofNullable(clienteRepository.findByCpf(cpf));
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Cliente createCliente(Cliente cliente) {
        Cliente createdCliente = clienteRepository.save(cliente);
        return createdCliente;
    }

    public ResponseEntity<Cliente> updateCliente(Long id, Cliente cliente) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            Cliente existingCliente = clienteOptional.get();
            existingCliente.setNome(cliente.getNome());
            existingCliente.setCpf(cliente.getCpf());
            Cliente updatedCliente = clienteRepository.save(existingCliente);
            return ResponseEntity.ok(updatedCliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public boolean deleteCliente(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
