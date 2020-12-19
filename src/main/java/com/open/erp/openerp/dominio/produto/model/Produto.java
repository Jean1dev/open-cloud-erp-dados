package com.open.erp.openerp.dominio.produto.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Produto implements Serializable {

    @Id
    private String id;

    private String nome;

    private String ca;

    private BigDecimal estoque;
}
