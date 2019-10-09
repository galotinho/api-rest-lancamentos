package com.sistema.money.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.money.api.event.RecursoCriadoEvent;
import com.sistema.money.api.exceptionhandler.MoneyExceptionHandler.Erro;
import com.sistema.money.api.model.Lancamento;
import com.sistema.money.api.repository.LancamentoRepository;
import com.sistema.money.api.repository.filter.LancamentoFilter;
import com.sistema.money.api.repository.projection.ResumoLancamento;
import com.sistema.money.api.service.LancamentoService;
import com.sistema.money.api.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		
		lancamentoFilter.setDescricao(lancamentoFilter.getDescricao() == null ? "" : lancamentoFilter.getDescricao());
		lancamentoFilter.setDataVencimentoDe(Optional.ofNullable(lancamentoFilter.getDataVencimentoDe()).orElse(lancamentoRepository.pesquisarMinDataVencimento())); 
		lancamentoFilter.setDataVencimentoAte(Optional.ofNullable(lancamentoFilter.getDataVencimentoAte()).orElse(lancamentoRepository.pesquisarMaxDataVencimento()));
				
		return lancamentoRepository.pesquisarPorFiltro(
				lancamentoFilter.getDescricao(), 
				lancamentoFilter.getDataVencimentoDe(), 
				lancamentoFilter.getDataVencimentoAte(), 
				pageable);
	}
	
	@GetMapping(params = "resumo")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		
		lancamentoFilter.setDescricao(lancamentoFilter.getDescricao() == null ? "" : lancamentoFilter.getDescricao());
		lancamentoFilter.setDataVencimentoDe(Optional.ofNullable(lancamentoFilter.getDataVencimentoDe()).orElse(lancamentoRepository.pesquisarMinDataVencimento())); 
		lancamentoFilter.setDataVencimentoAte(Optional.ofNullable(lancamentoFilter.getDataVencimentoAte()).orElse(lancamentoRepository.pesquisarMaxDataVencimento()));
				
		return lancamentoRepository.resumir(
				lancamentoFilter.getDescricao(), 
				lancamentoFilter.getDataVencimentoDe(), 
				lancamentoFilter.getDataVencimentoAte(), 
				pageable);
	}
	
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
	
		Optional<Lancamento> lancamento = lancamentoRepository.findById(codigo);
		
		if(lancamento.isPresent()) {
			return ResponseEntity.ok(lancamento.get());			
		}
		
		return ResponseEntity.notFound().build();	
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
	
	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		lancamentoRepository.deleteById(codigo);
	}
	
}
