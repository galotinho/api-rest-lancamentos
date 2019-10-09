package com.sistema.money.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sistema.money.api.model.Categoria;
import com.sistema.money.api.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Categoria> listar(){
		return categoriaRepository.findAll();
	}	
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		
		Optional<Categoria> a = categoriaRepository.findByNome(categoria.getNome());
		if(a.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta categoria já está cadastrada!");
		}
		
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(categoriaSalva.getCodigo()).toUri();
		
		response.setHeader("Location", uri.toASCIIString());
		
		return ResponseEntity.created(uri).body(categoriaSalva);
	}
	
	/*
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Categoria adicionar(@RequestBody Categoria categoria) {
		Optional<Categoria> a = categoriaRepository.findByNome(categoria.getNome());
		if(a.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta categoria já está cadastrada!");
		}
		return categoriaRepository.save(categoria);
	}
	*/
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<Categoria> buscar(@PathVariable Long codigo) {
		
		Optional<Categoria> categoria = categoriaRepository.findById(codigo);
		
		if(categoria.isPresent()) {
			return ResponseEntity.ok(categoria.get());			
		}
		
		return ResponseEntity.notFound().build();		
	}
	
	

}
