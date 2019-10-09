package com.sistema.money.api.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.sistema.money.api.model.TipoLancamento;

public interface ResumoLancamento {
	
	Long getCodigo();;
	String getDescricao();
	LocalDate getDataVencimento();
	LocalDate getDataPagamento();
	BigDecimal getValor();
	TipoLancamento getTipo();
	String getCategoria();
	String getPessoa();
	
	
}
