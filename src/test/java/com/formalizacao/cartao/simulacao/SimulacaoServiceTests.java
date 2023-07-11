package com.formalizacao.cartao.simulacao;

import com.formalizacao.cartao.model.Cliente;
import com.formalizacao.cartao.model.enums.ClassificacaoCartao;
import com.formalizacao.cartao.repository.ClienteRepository;
import com.formalizacao.cartao.service.SimulacaoService;
import com.formalizacao.cartao.util.Messages;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SimulacaoServiceTests {

    @InjectMocks
    private SimulacaoService simulacaoService;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(restTemplateBuilder.build()).thenReturn(restTemplate);
    }

    @Test
    public void testRealizarSimulacao_DeveRetornarMensagemSimulacao() {
        String cpf = "123456789";
        int pontuacaoTotal = 30;
        ClassificacaoCartao classificacaoCartao = ClassificacaoCartao.OURO;

        Cliente cliente = new Cliente();
        cliente.setUltimaSimulacao(null);

        ResponseEntity<Integer> response = ResponseEntity.ok(pontuacaoTotal);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET),
                Mockito.any(), Mockito.eq(Integer.class))).thenReturn(response);

        Mockito.when(clienteRepository.findByCpf(cpf)).thenReturn(cliente);

        String resultadoSimulacao = simulacaoService.realizarSimulacao(cpf);

        Assert.assertEquals(Messages.obterMensagemSimulacao(cpf, classificacaoCartao), resultadoSimulacao);
        Assert.assertEquals(classificacaoCartao, cliente.getUltimaSimulacao());
        Mockito.verify(clienteRepository, Mockito.times(1)).save(cliente);
    }

    @Test
    public void testRealizarSimulacao_ComClienteExistente_DeveAtualizarUltimaSimulacao() {
        String cpf = "123456789";
        int pontuacaoTotal = 20;
        ClassificacaoCartao classificacaoCartao = ClassificacaoCartao.PRATA;

        Cliente cliente = new Cliente();
        cliente.setUltimaSimulacao(null);

        ResponseEntity<Integer> response = ResponseEntity.ok(pontuacaoTotal);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET),
                Mockito.any(), Mockito.eq(Integer.class))).thenReturn(response);

        Mockito.when(clienteRepository.findByCpf(cpf)).thenReturn(cliente);

        String resultadoSimulacao = simulacaoService.realizarSimulacao(cpf);

        Assert.assertEquals(Messages.obterMensagemSimulacao(cpf, classificacaoCartao), resultadoSimulacao);
        Assert.assertEquals(classificacaoCartao, cliente.getUltimaSimulacao());
        Mockito.verify(clienteRepository, Mockito.times(1)).save(cliente);
    }

    @Test
    public void testRealizarSimulacao_ComClienteInexistente_NaoDeveAtualizarUltimaSimulacao() {
        String cpf = "123456789";
        int pontuacaoTotal = 10;
        ClassificacaoCartao classificacaoCartao = ClassificacaoCartao.BRONZE;

        Cliente cliente = null;

        ResponseEntity<Integer> response = ResponseEntity.ok(pontuacaoTotal);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET),
                Mockito.any(), Mockito.eq(Integer.class))).thenReturn(response);

        Mockito.when(clienteRepository.findByCpf(cpf)).thenReturn(cliente);

        String resultadoSimulacao = simulacaoService.realizarSimulacao(cpf);

        Assert.assertEquals(Messages.obterMensagemSimulacao(cpf, classificacaoCartao), resultadoSimulacao);
        Mockito.verify(clienteRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testClassificarCartao_DeveRetornarOuro() {
        int pontuacao = 30;
        ClassificacaoCartao expectedClassificacao = ClassificacaoCartao.OURO;

        ClassificacaoCartao classificacao = simulacaoService.classificarCartao(pontuacao);

        Assert.assertEquals(expectedClassificacao, classificacao);
    }

    @Test
    public void testClassificarCartao_DeveRetornarPrata() {
        int pontuacao = 20;
        ClassificacaoCartao expectedClassificacao = ClassificacaoCartao.PRATA;

        ClassificacaoCartao classificacao = simulacaoService.classificarCartao(pontuacao);

        Assert.assertEquals(expectedClassificacao, classificacao);
    }

    @Test
    public void testClassificarCartao_DeveRetornarBronze() {
        int pontuacao = 10;
        ClassificacaoCartao expectedClassificacao = ClassificacaoCartao.BRONZE;

        ClassificacaoCartao classificacao = simulacaoService.classificarCartao(pontuacao);

        Assert.assertEquals(expectedClassificacao, classificacao);
    }
}