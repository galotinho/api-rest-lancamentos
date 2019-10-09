package com.sistema.money.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "lancamento")
public class Lancamento {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotNull
	private String descricao;

	@NotNull
	@Column(name = "data_vencimento")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dataVencimento;

	@Column(name = "data_pagamento")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dataPagamento;

	@NotNull
	private BigDecimal valor;

	private String observacao;

	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoLancamento tipo;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "codigo_categoria")
	private Categoria categoria;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "codigo_pessoa")
	private Pessoa pessoa;
	
	
}