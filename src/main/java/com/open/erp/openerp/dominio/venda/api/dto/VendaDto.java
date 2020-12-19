package com.open.erp.openerp.dominio.venda.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VendaDto implements Serializable {

    private List<ItemVendaDto> itens;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class ItemVendaDto implements Serializable {

        private String produtoId;

        private BigDecimal quantidade;

        private BigDecimal valorUnitario;
    }
}
