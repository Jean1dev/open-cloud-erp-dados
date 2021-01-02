package com.open.erp.openerp.dominio.compra.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

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
public class Compra implements Serializable {

    @Id
    private String id;

    private LocalDate dataCompra;

    private BigDecimal valorTotal;

    private List<ItemCompra> itens;
}
