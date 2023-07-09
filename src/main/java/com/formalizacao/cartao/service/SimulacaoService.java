package com.formalizacao.cartao.service;

import com.formalizacao.cartao.model.SimulacaoResultado;
import com.formalizacao.cartao.model.enums.ClassificacaoCartao;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Random;

@Service
public class SimulacaoService {

    private final RestTemplate restTemplate;
    private final int LIMITE_PONTUACAO_OURO = 30;
    private final int LIMITE_PONTUACAO_PRATA = 15;

    public SimulacaoService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public SimulacaoResultado realizarSimulacao(String cpf) {
        int pontuacaoTotal = calcularPontuacaoCliente(cpf);
        ClassificacaoCartao classificacao;

        if (pontuacaoTotal >= LIMITE_PONTUACAO_OURO) {
            classificacao = ClassificacaoCartao.OURO;
        } else if (pontuacaoTotal >= LIMITE_PONTUACAO_PRATA) {
            classificacao = ClassificacaoCartao.PRATA;
        } else {
            classificacao = ClassificacaoCartao.BRONZE;
        }

        return new SimulacaoResultado(cpf, classificacao);
    }

    private int calcularPontuacaoCliente(String cpf) {
        int pontuacaoTotal = 0;

        pontuacaoTotal += chamarEndpoint("http://localhost:8080/simulacao/calcularPontuacaoRenda/" + cpf);
        pontuacaoTotal += chamarEndpoint("http://localhost:8080/simulacao/calcularPontuacaoNomeLimpo/" + cpf);
        pontuacaoTotal += chamarEndpoint("http://localhost:8080/simulacao/calcularPontuacaoDividas/" + cpf);
        pontuacaoTotal += chamarEndpoint("http://localhost:8080/simulacao/calcularPontuacaoHistoricoFinanceiro/" + cpf);
        pontuacaoTotal += chamarEndpoint("http://localhost:8080/simulacao/calcularPontuacaoPagamentoContas/" + cpf);
        pontuacaoTotal += chamarEndpoint("http://localhost:8080/simulacao/calcularPontuacaoRequisicoesCredito/" + cpf);

        return pontuacaoTotal;
    }

    private int chamarEndpoint(String url) {
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.GET, null, Integer.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Objects.requireNonNull(response.getBody());
            } else {
                throw new IllegalStateException("Erro na chamada do endpoint: " + url);
            }
        } catch (RestClientException e) {
            throw new IllegalStateException("Erro na chamada do endpoint: " + url, e);
        }
    }

    public int gerarValorAleatorio(String cpf){
        Random x = new Random();
        return x.nextInt(6);
    }
}