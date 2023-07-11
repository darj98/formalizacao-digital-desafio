package com.formalizacao.cartao.contratacao;

import com.formalizacao.cartao.controller.ContratacaoController;
import com.formalizacao.cartao.model.Contratacao;
import com.formalizacao.cartao.service.ContratacaoService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ContratacaoControllerTests {

    @InjectMocks
    private ContratacaoController contratacaoController;

    @Mock
    private ContratacaoService contratacaoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRealizarContratacao() {
        Contratacao contratacao = new Contratacao();
        String resultadoContratacao = "Contratação realizada com sucesso";
        Mockito.when(contratacaoService.realizarContratacao(contratacao)).thenReturn(resultadoContratacao);

        ResponseEntity<String> response = contratacaoController.realizarContratacao(contratacao);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(resultadoContratacao, response.getBody());
    }
}