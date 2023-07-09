package com.formalizacao.cartao.service;

import com.formalizacao.cartao.model.Cliente;
import com.formalizacao.cartao.model.SimulacaoResultado;
import com.formalizacao.cartao.model.enums.ClassificacaoCartao;
import com.formalizacao.cartao.util.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ContratacaoService {
    private final RestTemplate restTemplate;
    private final ClienteService clienteService;
    private final SimulacaoService simulacaoService;
    private static final String ENDPOINT_SIMULACAO = "http://localhost:8080/simulacao";

    @Autowired
    public ContratacaoService(RestTemplate restTemplate, ClienteService clienteService, SimulacaoService simulacaoService) {
        this.restTemplate = restTemplate;
        this.clienteService = clienteService;
        this.simulacaoService = simulacaoService;
    }

    public String realizarContratacao(String cpf, boolean utilizarUltimaSimulacao, String tipoCartao) {
        Cliente cliente = clienteService.getClienteByCpf(cpf).getBody();
        if (cliente == null) {
            return "CPF não cadastrado no banco ou inválido.";
        }

        if (clientePossuiDebito(cliente)) {
            return "Cliente possui débito pendente.";
        }

        String ultimaSimulacao = null;
        if (utilizarUltimaSimulacao) {
            ultimaSimulacao = String.valueOf(simulacaoService.getUltimaSimulacao(cpf));
            if (ultimaSimulacao == null) {
                return Messages.obterMensagemUltimaSimulacaoInexistente();
            }
        }else{
            ultimaSimulacao = chamarEndpoint(ENDPOINT_SIMULACAO + cpf);
        }

        ClassificacaoCartao classificacaoCartao;
        if (utilizarUltimaSimulacao) {
            classificacaoCartao = ultimaSimulacao.getClassificacaoCartao();
        } else {
            SimulacaoResultado novaSimulacao = simulacaoService.realizarSimulacao(cpf);
            classificacaoCartao = novaSimulacao.getClassificacaoCartao();
        }

        if (!isCartaoCompativel(classificacaoCartao, tipoCartao)) {
            return "O último cartão liberado foi " + classificacaoCartao.toString() + ". Seu pedido de cartão não é compatível.";
        }

        return "Parabéns! Você obteve sucesso na contratação do cartão " + tipoCartao + ".";
    }

    private boolean clientePossuiDebito(Cliente cliente) {
        return false;
    }

    private boolean isCartaoCompativel(ClassificacaoCartao classificacaoCartao, String tipoCartao) {
        return true;
    }

    private String chamarEndpoint(String url) {
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.GET, null, Integer.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Objects.requireNonNull(response.getBody());
            } else {
                throw new IllegalStateException(Messages.obterMensagemErroEndpoint() + url);
            }
        } catch (RestClientException e) {
            throw new IllegalStateException(Messages.obterMensagemErroEndpoint() + url, e);
        }
    }
}
