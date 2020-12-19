package com.open.erp.openerp.dominio.compra.model;

import com.open.erp.openerp.dominio.compra.api.dto.CompraDto;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemCompra implements Serializable {

    private String produtoId;

    private BigDecimal quantidade;

    private BigDecimal valorUnitario;

    private BigDecimal valorTotal;

    public static ItemCompra fromDto(CompraDto.ItemCompraDto itemCompraDto) {
        return ItemCompra.builder()
                .produtoId(itemCompraDto.getProdutoId())
                .valorUnitario(itemCompraDto.getValorUnitario())
                .quantidade(itemCompraDto.getQuantidade())
                .valorTotal(itemCompraDto.getQuantidade().multiply(itemCompraDto.getValorUnitario()))
                .build();
    }
}
