package com.open.erp.openerp.dominio.venda.model;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteAgregado implements Serializable {

    private String idCliente;

    private String nome;
}
