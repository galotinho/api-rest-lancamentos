package com.sistema.money.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.money.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria,Long>{
	
	Optional<Categoria> findByNome(String nome);

}
