package com.formalizacao.cartao.cliente;

import com.formalizacao.cartao.model.Cliente;
import com.formalizacao.cartao.repository.ClienteRepository;
import com.formalizacao.cartao.service.ClienteService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ClienteServiceTests {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllClientes() {
        List<Cliente> clientes = Arrays.asList(new Cliente(), new Cliente());
        Mockito.when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> result = clienteService.getAllClientes();

        Assert.assertEquals(clientes, result);
    }

    @Test
    public void testGetClienteById() {
        Long id = 1L;
        Optional<Cliente> clienteOptional = Optional.of(new Cliente());
        Mockito.when(clienteRepository.findById(id)).thenReturn(clienteOptional);

        ResponseEntity<Cliente> result = clienteService.getClienteById(id);

        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assert.assertEquals(clienteOptional.get(), result.getBody());
    }

    @Test
    public void testGetClienteByCpf() {
        String cpf = "123456789";
        Cliente cliente = new Cliente();
        Mockito.when(clienteRepository.findByCpf(cpf)).thenReturn(cliente);

        ResponseEntity<Cliente> result = clienteService.getClienteByCpf(cpf);

        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assert.assertEquals(cliente, result.getBody());
    }

    @Test
    public void testCreateCliente() {
        Cliente cliente = new Cliente();
        Mockito.when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.createCliente(cliente);

        Assert.assertEquals(cliente, result);
    }

    @Test
    public void testUpdateCliente() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        Optional<Cliente> clienteOptional = Optional.of(new Cliente());
        Mockito.when(clienteRepository.findById(id)).thenReturn(clienteOptional);
        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(cliente);

        ResponseEntity<Cliente> result = clienteService.updateCliente(id, cliente);

        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assert.assertEquals(cliente, result.getBody());
    }

    @Test
    public void testDeleteCliente() {
        Long id = 1L;
        Optional<Cliente> clienteOptional = Optional.of(new Cliente());
        Mockito.when(clienteRepository.findById(id)).thenReturn(clienteOptional);

        boolean result = clienteService.deleteCliente(id);

        Assert.assertTrue(result);
        Mockito.verify(clienteRepository, Mockito.times(1)).deleteById(id);
    }
}
