package com.formalizacao.cartao.util;

import com.formalizacao.cartao.model.enums.ClassificacaoCartao;

public class Messages {
    public static String obterMensagemSimulacao(String cpf, ClassificacaoCartao classificacao) {
        return String.format("Após a simulação, o nível de cartão máximo disponibilizado para o cliente %s é o %s", cpf, classificacao);
    }

    public static String obterMensagemUltimaSimulacao(String cpf, ClassificacaoCartao classificacao) {
        return String.format("A última simulação para o cliente %s foi do cartão tipo: %s", cpf, classificacao);
    }

    public static String obterMensagemErroEndpoint() {
        return "Ocorreu um erro na chamada do endpoint: ";
    }

    public static String obterMensagemClienteDeletado() {
        return "Cliente deletado com sucesso!";
    }

    public static String obterMensagemUltimaSimulacaoInexistente() {
        return "O cliente em questão não possui uma simulação recente";
    }

    public static String obterMensagemNomeCliente() {
        return "O nome do cliente é obrigatório!";
    }

    public static String obterMensagemCpfCliente() {
        return "O cpf do cliente é obrigatório!";
    }
}
