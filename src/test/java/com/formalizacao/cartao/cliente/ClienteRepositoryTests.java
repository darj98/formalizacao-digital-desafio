package com.formalizacao.cartao.cliente;

import com.formalizacao.cartao.model.Cliente;
import com.formalizacao.cartao.repository.ClienteRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.JpaContext;

public class ClienteRepositoryTests {

    private ClienteRepository clienteRepository;

    @Mock
    private JpaContext jpaContext;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        clienteRepository = Mockito.mock(ClienteRepository.class);
    }

    @Test
    public void testExistsByCpf() {
        String cpfExistente = "123456789";
        String cpfInexistente = "987654321";

        Mockito.when(clienteRepository.existsByCpf(cpfExistente)).thenReturn(true);
        Mockito.when(clienteRepository.existsByCpf(cpfInexistente)).thenReturn(false);

        boolean resultExistente = clienteRepository.existsByCpf(cpfExistente);
        boolean resultInexistente = clienteRepository.existsByCpf(cpfInexistente);

        Assert.assertTrue(resultExistente);
        Assert.assertFalse(resultInexistente);
    }

    @Test
    public void testFindByCpf() {
        String cpf = "123456789";
        Cliente cliente = new Cliente();
        Mockito.when(clienteRepository.findByCpf(cpf)).thenReturn(cliente);

        Cliente result = clienteRepository.findByCpf(cpf);

        Assert.assertEquals(cliente, result);
    }
}