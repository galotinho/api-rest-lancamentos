package com.sistema.money.api.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sistema.money.api.model.Lancamento;
import com.sistema.money.api.repository.projection.ResumoLancamento;


public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	
	@Query("select l from Lancamento l "
			+ "where l.descricao like %:descricao% "
			+ "AND l.dataVencimento >= :dataVencimentoDe "
			+ "AND l.dataVencimento <= :dataVencimentoAte")
	Page<Lancamento> pesquisarPorFiltro(String descricao, LocalDate dataVencimentoDe, LocalDate dataVencimentoAte, Pageable pageable);
		
	@Query("SELECT MAX(l.dataVencimento) FROM Lancamento l")
	LocalDate pesquisarMaxDataVencimento();
	
	@Query("SELECT MIN(l.dataVencimento) FROM Lancamento l")
	LocalDate pesquisarMinDataVencimento();

	@Query("select l.codigo as codigo, l.descricao as descricao, l.dataVencimento as dataVencimento, "
			+ "l.dataPagamento as dataPagamento, l.valor as valor, l.tipo as tipo,"
			+ "l.categoria.nome as categoria, l.pessoa.nome as pessoa from Lancamento l "
			+ "where l.descricao like %:descricao% "
			+ "AND l.dataVencimento >= :dataVencimentoDe "
			+ "AND l.dataVencimento <= :dataVencimentoAte")
	Page<ResumoLancamento> resumir(String descricao, LocalDate dataVencimentoDe, LocalDate dataVencimentoAte,
			Pageable pageable);
}