package com.open.erp.openerp.dominio.venda.api.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
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
