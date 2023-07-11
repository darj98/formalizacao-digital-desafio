package com.formalizacao.cartao.contratacao;

import com.formalizacao.cartao.model.Cliente;
import com.formalizacao.cartao.model.Contratacao;
import com.formalizacao.cartao.model.enums.ClassificacaoCartao;
import com.formalizacao.cartao.repository.ClienteRepository;
import com.formalizacao.cartao.service.ContratacaoService;
import com.formalizacao.cartao.service.ClienteService;
import com.formalizacao.cartao.service.SimulacaoService;
import com.formalizacao.cartao.util.Messages;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;

public class ContratacaoServiceTests {

    @InjectMocks
    private ContratacaoService contratacaoService;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private ClienteService clienteService;

    @Mock
    private SimulacaoService simulacaoService;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRealizarContratacao_ClienteInexistente() {
        Contratacao contratacao = new Contratacao();
        contratacao.setCpf("123456789");
        Mockito.when(clienteService.getClienteByCpf(contratacao.getCpf())).thenReturn(ResponseEntity.notFound().build());

        String resultadoContratacao = contratacaoService.realizarContratacao(contratacao);

        Assert.assertEquals(Messages.obterMensagemCpfInválido(), resultadoContratacao);
    }

    @Test
    public void testRealizarContratacao_CpfInexistente() {
        Contratacao contratacao = new Contratacao();
        contratacao.setCpf("123456789");
        Cliente cliente = new Cliente();
        Mockito.when(clienteRepository.existsByCpf(contratacao.getCpf())).thenReturn(false);
        Mockito.when(clienteService.getClienteByCpf(contratacao.getCpf())).thenReturn(ResponseEntity.ok(cliente));

        String resultadoContratacao = contratacaoService.realizarContratacao(contratacao);

        Assert.assertEquals(Messages.obterMensagemCpfInexistenteInvalido(), resultadoContratacao);
    }

    @Test
    public void testRealizarContratacao_DeveRealizarSimulacao() {
        Contratacao contratacao = new Contratacao();
        contratacao.setCpf("123456789");
        contratacao.setTipoCartao("OURO");

        Cliente cliente = new Cliente();
        cliente.setUltimaSimulacao(ClassificacaoCartao.OURO);

        ClienteService clienteServiceMock = Mockito.mock(ClienteService.class);
        Mockito.when(clienteServiceMock.getClienteByCpf(contratacao.getCpf()))
                .thenReturn(ResponseEntity.ok(cliente));

        Mockito.when(clienteServiceMock.getClienteByCpf("CPF_INEXISTENTE"))
                .thenReturn(ResponseEntity.notFound().build());

        SimulacaoService simulacaoServiceMock = Mockito.mock(SimulacaoService.class);
        Mockito.when(simulacaoServiceMock.realizarSimulacao(contratacao.getCpf()))
                .thenReturn(String.valueOf(ClassificacaoCartao.OURO));

        ClienteRepository clienteRepositoryMock = Mockito.mock(ClienteRepository.class);
        Mockito.when(clienteRepositoryMock.existsByCpf(contratacao.getCpf()))
                .thenReturn(true);

        ContratacaoService contratacaoService = new ContratacaoService(restTemplateBuilder, clienteServiceMock,
                simulacaoServiceMock, clienteRepositoryMock);

        String resultadoContratacao = contratacaoService.realizarContratacao(contratacao);

        Assert.assertEquals(Messages.obterMensagemSucessoContratacao() + contratacao.getTipoCartao(),
                resultadoContratacao);
        Mockito.verify(simulacaoServiceMock,
                Mockito.times(1)).realizarSimulacao(contratacao.getCpf());
    }

    @Test
    public void testRealizarContratacao_DeveUtilizarUltimaSimulacao() {
        Contratacao contratacao = new Contratacao();
        contratacao.setCpf("123456789");
        contratacao.setTipoCartao("OURO");
        Cliente cliente = new Cliente();
        cliente.setUltimaSimulacao(ClassificacaoCartao.PRATA);

        Mockito.when(clienteRepository.existsByCpf(contratacao.getCpf())).thenReturn(true);
        Mockito.when(clienteService.getClienteByCpf(contratacao.getCpf())).thenReturn(ResponseEntity.ok(cliente));
        Mockito.when(simulacaoService.realizarSimulacao(contratacao.getCpf())).thenReturn(
                String.valueOf(ClassificacaoCartao.PRATA));

        String resultadoContratacao = contratacaoService.realizarContratacao(contratacao);

        Assert.assertEquals(Messages.obterMensagemContratacaoNegada() + ClassificacaoCartao.PRATA,
                resultadoContratacao);
        Mockito.verify(simulacaoService, Mockito.times(1)).realizarSimulacao(Mockito.anyString());
    }

    @Test
    public void testRealizarContratacao_TipoCartaoInvalido() {
        Contratacao contratacao = new Contratacao();
        contratacao.setCpf("123456789");
        contratacao.setTipoCartao("INVALIDO");
        Cliente cliente = new Cliente();
        Mockito.when(clienteRepository.existsByCpf(contratacao.getCpf())).thenReturn(true);
        Mockito.when(clienteService.getClienteByCpf(contratacao.getCpf())).thenReturn(ResponseEntity.ok(cliente));

        String resultadoContratacao = contratacaoService.realizarContratacao(contratacao);

        Assert.assertEquals(Messages.obterMensagemTipoCartaoInvalido(), resultadoContratacao);
    }

    @Test
    public void testRealizarContratacao_DeveContratarCartao() {
        Contratacao contratacao = new Contratacao();
        contratacao.setCpf("123456789");
        contratacao.setTipoCartao("OURO");
        Cliente cliente = new Cliente();
        cliente.setUltimaSimulacao(ClassificacaoCartao.OURO);
        Mockito.when(clienteRepository.existsByCpf(contratacao.getCpf())).thenReturn(true);
        Mockito.when(clienteService.getClienteByCpf(contratacao.getCpf())).thenReturn(ResponseEntity.ok(cliente));

        String resultadoContratacao = contratacaoService.realizarContratacao(contratacao);

        String mensagemEsperada = Messages.obterMensagemSucessoContratacao() + contratacao.getTipoCartao();
        Assert.assertEquals(mensagemEsperada, resultadoContratacao);
    }
}
