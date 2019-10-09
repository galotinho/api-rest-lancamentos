package com.sistema.money.api.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LancamentoFilter {

	private String descricao;
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dataVencimentoDe;
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dataVencimentoAte;
	
}
