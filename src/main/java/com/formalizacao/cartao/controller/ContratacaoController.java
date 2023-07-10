package com.formalizacao.cartao.controller;

import com.formalizacao.cartao.model.Contratacao;
import com.formalizacao.cartao.service.ContratacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contratacao")
public class ContratacaoController {

    private final ContratacaoService contratacaoService;

    @Autowired
    public ContratacaoController(ContratacaoService contratacaoService) {
        this.contratacaoService = contratacaoService;
    }

    @PostMapping
    public ResponseEntity<String> realizarContratacao(@RequestBody Contratacao contratacao) {
        String resultadoContratacao = contratacaoService.realizarContratacao(contratacao);
        return ResponseEntity.ok(resultadoContratacao);
    }
}
