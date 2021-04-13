package com.open.erp.openerp.dominio.titulosreceber.api.dto;

import com.open.erp.openerp.dominio.titulosreceber.model.TituloAReceber;
import com.open.erp.openerp.dominio.venda.model.Venda;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class TituloComVendaDto implements Serializable {

    private TituloAReceber tituloAReceber;
    private Venda venda;
}
