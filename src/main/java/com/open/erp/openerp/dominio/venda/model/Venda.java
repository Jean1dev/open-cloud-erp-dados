package com.open.erp.openerp.dominio.venda.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Venda implements Serializable {

    @Id
    private String id;

    private LocalDate dataVenda;

    private BigDecimal valorTotal;

    private List<ItemVenda> itens;
}
