package com.open.erp.openerp.dominio.relatorios.vendas.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class VendaAgrupaPorCliente {

    private String nomeCliente;

    private BigDecimal total;
}
