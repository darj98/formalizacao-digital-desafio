package com.formalizacao.cartao.cliente;

import com.formalizacao.cartao.controller.ClienteController;
import com.formalizacao.cartao.model.Cliente;
import com.formalizacao.cartao.model.enums.ClassificacaoCartao;
import com.formalizacao.cartao.repository.ClienteRepository;
import com.formalizacao.cartao.service.ClienteService;
import com.formalizacao.cartao.util.Messages;
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

public class ClienteControllerTests {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
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
        Mockito.when(clienteService.getAllClientes()).thenReturn(clientes);

        List<Cliente> response = clienteController.getAllClientes();

        Assert.assertEquals(clientes, response);
    }

    @Test
    public void testGetClienteById() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        Mockito.when(clienteService.getClienteById(id)).thenReturn(ResponseEntity.ok(cliente));

        ResponseEntity<Cliente> response = clienteController.getClienteById(id);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(cliente, response.getBody());
    }

    @Test
    public void testCreateCliente() {
        // Cenário válido
        Cliente cliente = new Cliente();
        Mockito.when(clienteService.createCliente(Mockito.any(Cliente.class))).thenReturn(cliente);

        ResponseEntity<?> response = clienteController.createCliente(cliente);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(cliente, response.getBody());

        // Cenário de nome vazio
        Cliente clienteNomeVazio = new Cliente();
        Mockito.when(clienteService.createCliente(Mockito.eq(clienteNomeVazio)))
                .thenThrow(new IllegalArgumentException(Messages.obterMensagemNomeCliente()));

        ResponseEntity<?> responseNomeVazio = clienteController.createCliente(clienteNomeVazio);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseNomeVazio.getStatusCode());
        Assert.assertEquals(Messages.obterMensagemNomeCliente(), responseNomeVazio.getBody());

        // Cenário de CPF vazio
        Cliente clienteCpfVazio = new Cliente();
        Mockito.when(clienteService.createCliente(Mockito.eq(clienteCpfVazio)))
                .thenThrow(new IllegalArgumentException(Messages.obterMensagemCpfCliente()));

        ResponseEntity<?> responseCpfVazio = clienteController.createCliente(clienteCpfVazio);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseCpfVazio.getStatusCode());
        Assert.assertEquals(Messages.obterMensagemCpfCliente(), responseCpfVazio.getBody());

        // Cenário de CPF existente
        Cliente clienteCpfExistente = new Cliente();
        Mockito.when(clienteService.createCliente(Mockito.eq(clienteCpfExistente)))
                .thenThrow(new IllegalArgumentException(Messages.obterMensagemClienteExistente()));

        ResponseEntity<?> responseCpfExistente = clienteController.createCliente(clienteCpfExistente);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseCpfExistente.getStatusCode());
        Assert.assertEquals(Messages.obterMensagemClienteExistente(), responseCpfExistente.getBody());
    }

    @Test
    public void testUpdateCliente() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        Mockito.when(clienteService.updateCliente(id, cliente)).thenReturn(ResponseEntity.ok(cliente));

        ResponseEntity<Cliente> response = clienteController.updateCliente(id, cliente);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(cliente, response.getBody());
    }

    @Test
    public void testDeleteCliente() {
        Long id = 1L;
        Mockito.when(clienteService.deleteCliente(id)).thenReturn(true);

        ResponseEntity<String> response = clienteController.deleteCliente(id);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("Cliente deletado com sucesso!", response.getBody());
    }

    @Test
    public void testGetUltimaSimulacao() {
        String cpf = "123456789";
        Cliente cliente = new Cliente();
        cliente.setUltimaSimulacao(ClassificacaoCartao.PRATA);
        Mockito.when(clienteService.getClienteByCpf(cpf)).thenReturn(ResponseEntity.ok(cliente));

        ResponseEntity<String> response = clienteController.getUltimaSimulacao(cpf);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals("A última simulação para o cliente 123456789 foi do cartão tipo: PRATA",
                response.getBody());
    }
}
