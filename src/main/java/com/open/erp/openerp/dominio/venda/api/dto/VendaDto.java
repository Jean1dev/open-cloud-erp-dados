package com.open.erp.openerp.dominio.venda.api.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendaDto implements Serializable {

    private List<ItemVendaDto> itens;

    private String cliente;

    private BigDecimal valorRecebido;

    private Boolean mobile;

    private LocalDate dataLimitePagamento;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static final class ItemVendaDto implements Serializable {

        private String produtoId;

        private BigDecimal quantidade;

        private BigDecimal valorUnitario;
    }
}
