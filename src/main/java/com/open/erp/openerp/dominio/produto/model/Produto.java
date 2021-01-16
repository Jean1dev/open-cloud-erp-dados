package com.open.erp.openerp.dominio.produto.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "O nome é obrigatorio")
    private String nome;

    private String ca;

    private BigDecimal estoque;

    private BigDecimal valorVenda;

    private BigDecimal valorCompra;
}
