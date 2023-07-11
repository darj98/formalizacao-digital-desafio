package com.formalizacao.cartao.service;

import com.formalizacao.cartao.model.Cliente;
import com.formalizacao.cartao.model.Contratacao;
import com.formalizacao.cartao.model.enums.ClassificacaoCartao;
import com.formalizacao.cartao.repository.ClienteRepository;
import com.formalizacao.cartao.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class ContratacaoService {
    private final RestTemplate restTemplate;
    private final ClienteService clienteService;
    private final SimulacaoService simulacaoService;
    private final ClienteRepository clienteRepository;
    private static final String ENDPOINT_SIMULACAO = "http://localhost:8080/simulacao/";
    private static final String OURO = "OURO";
    private static final String PRATA = "PRATA";

    @Autowired
    public ContratacaoService(RestTemplateBuilder restTemplateBuilder, ClienteService clienteService,
                              SimulacaoService simulacaoService, ClienteRepository clienteRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.clienteService = clienteService;
        this.simulacaoService = simulacaoService;
        this.clienteRepository = clienteRepository;
    }

    public String realizarContratacao(Contratacao contratacao) {
        Cliente cliente = clienteService.getClienteByCpf(contratacao.getCpf()).getBody();

        if (cliente == null) {
            return Messages.obterMensagemCpfInválido();
        }else if (!clienteRepository.existsByCpf(contratacao.getCpf())) {
            return Messages.obterMensagemCpfInexistenteInvalido();
        }

        if (clientePossuiDebito(cliente)) {
            return Messages.obterMensagemDebitoPendente();
        }

        if(!isTipoCartaoEnviadoValido(contratacao.getTipoCartao())){
            return Messages.obterMensagemTipoCartaoInvalido();
        }

        ClassificacaoCartao simulacao;
        if (contratacao.isUtilizarUltimaSimulacao()) {
            simulacao = cliente.getUltimaSimulacao();
            if (simulacao == null) {
                return Messages.obterMensagemUltimaSimulacaoInexistente();
            }
        }else{
            simulacaoService.realizarSimulacao(contratacao.getCpf());
            simulacao = cliente.getUltimaSimulacao();
        }

        if (!isCartaoCompativel(simulacao, contratacao.getTipoCartao())) {
            return Messages.obterMensagemContratacaoNegada() + simulacao;
        }

        return Messages.obterMensagemSucessoContratacao() + contratacao.getTipoCartao();
    }

    private boolean clientePossuiDebito(Cliente cliente) {
        Random random = new Random();
        return random.nextBoolean();
    }

    private boolean isCartaoCompativel(ClassificacaoCartao cartaoLiberado, String cartaoRequisitado) {
        return switch (cartaoRequisitado) {
            case OURO -> cartaoLiberado == ClassificacaoCartao.OURO;
            case PRATA -> cartaoLiberado == ClassificacaoCartao.PRATA ||
                    cartaoLiberado == ClassificacaoCartao.OURO;
            default -> true;
        };
    }

    private boolean isTipoCartaoEnviadoValido(String cartaoSolicitado) {
        try {
            ClassificacaoCartao.valueOf(cartaoSolicitado.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
