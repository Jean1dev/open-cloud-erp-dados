package com.open.erp.openerp.dominio.compra.api.dto;

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
public class CompraDto implements Serializable {

    private List<ItemCompraDto> itens;

    private String fornecedor;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class ItemCompraDto implements Serializable {

        private String produtoId;

        private BigDecimal quantidade;

        private BigDecimal valorUnitario;
    }
}
