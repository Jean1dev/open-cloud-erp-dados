package com.open.erp.openerp.dominio.orcamento.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransformarOrcamentoEmVendaDto {

    private String orcamentoId;

    private BigDecimal valorRecebido;

    private String clienteId;
}
