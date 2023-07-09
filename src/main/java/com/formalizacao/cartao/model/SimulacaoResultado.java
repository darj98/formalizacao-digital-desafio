package com.formalizacao.cartao.model;

import com.formalizacao.cartao.model.enums.ClassificacaoCartao;

public class SimulacaoResultado {
    private String cpf;
    private ClassificacaoCartao classificacao;

    public SimulacaoResultado(String cpf, ClassificacaoCartao classificacao) {
        this.cpf = cpf;
        this.classificacao = classificacao;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
