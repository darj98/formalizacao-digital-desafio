package com.formalizacao.cartao.model;

import com.formalizacao.cartao.model.enums.ClassificacaoCartao;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "O nome do cliente é obrigatório")
    private String nome;
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF inválido")
    private String cpf;
    private ClassificacaoCartao ultimaSimulacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public ClassificacaoCartao getUltimaSimulacao() {
        return ultimaSimulacao;
    }

    public void setUltimaSimulacao(ClassificacaoCartao ultimaSimulacao) {
        this.ultimaSimulacao = ultimaSimulacao;
    }
}