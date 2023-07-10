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

    public static String obterMensagemCpfInválido() {
        return "CPF não cadastrado no banco ou inválido!";
    }

    public static String obterMensagemDebitoPendente() {
        return "Cliente possui débito pendente!";
    }
    public static String obterMensagemSucessoContratacao() {
        return "Parabéns! Você obteve sucesso na contratação do cartão: ";
    }

    public static String obterMensagemContratacaoNegada() {
        return "Seu pedido de cartão não é compatível pois o último cartão liberado foi o: ";
    }

    public static String obterMensagemTipoCartaoInvalido() {
        return "O Tipo de cartão requisitado é inválido!";
    }

    public static String obterMensagemCpfInexistenteInvalido() {
        return "CPF não cadastrado no banco ou inválido.";
    }

    public static String obterMensagemClienteExistente() {
        return "CPF informado já existe nos registros!";
    }
}
