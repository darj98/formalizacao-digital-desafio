package com.formalizacao.cartao.service;

import com.formalizacao.cartao.model.Cliente;
import com.formalizacao.cartao.model.enums.ClassificacaoCartao;
import com.formalizacao.cartao.repository.ClienteRepository;
import com.formalizacao.cartao.util.Messages;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SimulacaoService {
    private final RestTemplate restTemplate;
    private final ClienteRepository clienteRepository;
    private final int LIMITE_PONTUACAO_OURO = 25;
    private final int LIMITE_PONTUACAO_PRATA = 15;
    private final int LIMITE_VALOR_ALEATORIO = 6;
    private static final String ENDPOINT_CALCULAR_PONTUACAO_RENDA = "http://localhost:8080/simulacao/calcularPontuacaoRenda/";
    private static final String ENDPOINT_CALCULAR_PONTUACAO_NOME_LIMPO = "http://localhost:8080/simulacao/calcularPontuacaoNomeLimpo/";
    private static final String ENDPOINT_CALCULAR_PONTUACAO_DIVIDAS = "http://localhost:8080/simulacao/calcularPontuacaoDividas/";
    private static final String ENDPOINT_CALCULAR_PONTUACAO_HISTORICO_FINANCEIRO = "http://localhost:8080/simulacao/calcularPontuacaoHistoricoFinanceiro/";
    private static final String ENDPOINT_CALCULAR_PONTUACAO_PAGAMENTO_CONTAS = "http://localhost:8080/simulacao/calcularPontuacaoPagamentoContas/";
    private static final String ENDPOINT_CALCULAR_PONTUACAO_REQUISICOES_CREDITO = "http://localhost:8080/simulacao/calcularPontuacaoRequisicoesCredito/";
    private static final String ENDPOINT_TRAZER_ULTIMA_SIMULACAO_COMECO = "http://localhost:8080/clientes/";
    private static final String ENDPOINT_TRAZER_ULTIMA_SIMULACAO_FIM = "/ultimaSimulacao";

    public SimulacaoService(RestTemplateBuilder restTemplateBuilder, ClienteRepository clienteRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.clienteRepository = clienteRepository;
    }

    public String realizarSimulacao(String cpf) {
        int pontuacaoTotal = calcularPontuacaoCliente(cpf);
        ClassificacaoCartao classificacao;

        switch (classificarCartao(pontuacaoTotal)) {
            case OURO -> classificacao = ClassificacaoCartao.OURO;
            case PRATA -> classificacao = ClassificacaoCartao.PRATA;
            default -> classificacao = ClassificacaoCartao.BRONZE;
        }

        Cliente cliente = clienteRepository.findByCpf(cpf);
        if (cliente != null) {
            cliente.setUltimaSimulacao(classificacao);
            clienteRepository.save(cliente);
        }

        return String.format(Messages.obterMensagemSimulacao(cpf,classificacao));
    }

    private ClassificacaoCartao classificarCartao(int pontuacao) {
        if (pontuacao >= LIMITE_PONTUACAO_OURO) {
            return ClassificacaoCartao.OURO;
        } else if (pontuacao >= LIMITE_PONTUACAO_PRATA) {
            return ClassificacaoCartao.PRATA;
        } else {
            return ClassificacaoCartao.BRONZE;
        }
    }

    private int calcularPontuacaoCliente(String cpf) {
        int pontuacaoTotal = 0;

        pontuacaoTotal += chamarEndpoint(ENDPOINT_CALCULAR_PONTUACAO_RENDA + cpf);
        pontuacaoTotal += chamarEndpoint(ENDPOINT_CALCULAR_PONTUACAO_NOME_LIMPO + cpf);
        pontuacaoTotal += chamarEndpoint(ENDPOINT_CALCULAR_PONTUACAO_DIVIDAS + cpf);
        pontuacaoTotal += chamarEndpoint(ENDPOINT_CALCULAR_PONTUACAO_HISTORICO_FINANCEIRO + cpf);
        pontuacaoTotal += chamarEndpoint(ENDPOINT_CALCULAR_PONTUACAO_PAGAMENTO_CONTAS + cpf);
        pontuacaoTotal += chamarEndpoint(ENDPOINT_CALCULAR_PONTUACAO_REQUISICOES_CREDITO + cpf);

        return pontuacaoTotal;
    }

    private int chamarEndpoint(String url) {
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

    public ClassificacaoCartao getUltimaSimulacao(String cpf){
        String url = ENDPOINT_TRAZER_ULTIMA_SIMULACAO_COMECO + cpf + ENDPOINT_TRAZER_ULTIMA_SIMULACAO_FIM;
        try {
            ResponseEntity<ClassificacaoCartao> response = restTemplate.exchange(url,
                    HttpMethod.GET, null, ClassificacaoCartao.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return Objects.requireNonNull(response.getBody());
            } else {
                throw new IllegalStateException(Messages.obterMensagemErroEndpoint() + url);
            }
        } catch (RestClientException e) {
            throw new IllegalStateException(Messages.obterMensagemErroEndpoint() + url, e);
        }
    }

    public int gerarValorAleatorio(String cpf) {
        return ThreadLocalRandom.current().nextInt(LIMITE_VALOR_ALEATORIO);
    }
}