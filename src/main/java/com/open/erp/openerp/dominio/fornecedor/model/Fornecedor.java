package com.open.erp.openerp.dominio.fornecedor.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Fornecedor implements Serializable {

    @Id
    private String id;

    private String nome;

    private String telefone;
}
