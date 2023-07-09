package com.formalizacao.cartao.controller;

import com.formalizacao.cartao.model.SimulacaoResultado;
import com.formalizacao.cartao.repository.ClienteRepository;
import com.formalizacao.cartao.service.SimulacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulacao")
public class SimulacaoController {
    private final SimulacaoService simulacaoService;

    private final ClienteRepository clienteRepository;

    public SimulacaoController(SimulacaoService simulacaoService, ClienteRepository clienteRepository) {
        this.simulacaoService = simulacaoService;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    public ResponseEntity<String> realizarSimulacao(@RequestParam("cpf") String cpf) {
        boolean cpfExists = clienteRepository.existsByCpf(cpf);

        if (!cpfExists) {
            return ResponseEntity.badRequest().body("CPF não cadastrado no banco ou inválido.");
        }

        SimulacaoResultado resultado = simulacaoService.realizarSimulacao(cpf);
        return ResponseEntity.ok(resultado.toString());
    }

    @GetMapping("/calcularPontuacaoRenda/{cpf}")
    public int calcularPontuacaoRenda(@PathVariable String cpf) {
        return simulacaoService.gerarValorAleatorio(cpf);
    }

    @GetMapping("/calcularPontuacaoNomeLimpo/{cpf}")
    public int calcularPontuacaoNomeLimpo(@PathVariable String cpf) {
        return simulacaoService.gerarValorAleatorio(cpf);
    }

    @GetMapping("/calcularPontuacaoDividas/{cpf}")
    public int calcularPontuacaoDividas(@PathVariable String cpf) {
        return simulacaoService.gerarValorAleatorio(cpf);
    }

    @GetMapping("/calcularPontuacaoHistoricoFinanceiro/{cpf}")
    public int calcularPontuacaoHistoricoFinanceiro(@PathVariable String cpf) {
        return simulacaoService.gerarValorAleatorio(cpf);
    }

    @GetMapping("/calcularPontuacaoPagamentoContas/{cpf}")
    public int calcularPontuacaoPagamentoContas(@PathVariable String cpf) {
        return simulacaoService.gerarValorAleatorio(cpf);
    }

    @GetMapping("/calcularPontuacaoRequisicoesCredito/{cpf}")
    public int calcularPontuacaoRequisicoesCredito(@PathVariable String cpf) {
        return simulacaoService.gerarValorAleatorio(cpf);
    }
}