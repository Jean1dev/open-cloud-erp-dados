package com.open.erp.openerp.dominio.venda.model;

import com.open.erp.openerp.dominio.venda.api.dto.VendaDto;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemVenda {

    private String produtoId;

    private BigDecimal quantidade;

    private BigDecimal valorUnitario;

    private BigDecimal valorTotal;

    public static ItemVenda fromDto(VendaDto.ItemVendaDto item) {
        return ItemVenda.builder()
                .produtoId(item.getProdutoId())
                .valorUnitario(item.getValorUnitario())
                .quantidade(item.getQuantidade())
                .valorTotal(item.getQuantidade().multiply(item.getValorUnitario()))
                .build();
    }
}
