package com.formalizacao.cartao.repository;

import com.formalizacao.cartao.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCpf(String cpf);

    Cliente findByCpf(String cpf);
}
