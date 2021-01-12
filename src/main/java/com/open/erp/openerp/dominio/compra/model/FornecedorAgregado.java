package com.open.erp.openerp.dominio.compra.model;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FornecedorAgregado implements Serializable {

    private String fornecedorId;

    private String nome;
}
