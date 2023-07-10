package com.formalizacao.cartao.model;

public class Contratacao {
    private String cpf;
    private boolean utilizarUltimaSimulacao;
    private String tipoCartao;

    public Contratacao(String cpf, boolean utilizarUltimaSimulacao, String tipoCartao) {
        this.cpf = cpf;
        this.utilizarUltimaSimulacao = utilizarUltimaSimulacao;
        this.tipoCartao = tipoCartao;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isUtilizarUltimaSimulacao() {
        return utilizarUltimaSimulacao;
    }

    public void setUtilizarUltimaSimulacao(boolean utilizarUltimaSimulacao) {
        this.utilizarUltimaSimulacao = utilizarUltimaSimulacao;
    }

    public String getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(String tipoCartao) {
        this.tipoCartao = tipoCartao;
    }
}
