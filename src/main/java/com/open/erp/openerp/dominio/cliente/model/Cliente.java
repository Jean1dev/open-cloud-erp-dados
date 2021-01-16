package com.open.erp.openerp.dominio.cliente.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Cliente implements Serializable {

    @Id
    private String id;

    @NotBlank(message = "Nome é obrigatorio")
    private String nome;

    private String telefone;
}
