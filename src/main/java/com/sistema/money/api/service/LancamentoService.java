package com.sistema.money.api.service;

import org.springframework.stereotype.Service;

import com.sistema.money.api.model.Lancamento;
import com.sistema.money.api.model.Pessoa;
import com.sistema.money.api.repository.LancamentoRepository;
import com.sistema.money.api.repository.PessoaRepository;
import com.sistema.money.api.service.exception.PessoaInexistenteOuInativaException;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class LancamentoService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired 
	private LancamentoRepository lancamentoRepository;

	public Lancamento salvar(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo()).get();
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		return lancamentoRepository.save(lancamento);
	}
	
}