package com.sistema.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.money.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
