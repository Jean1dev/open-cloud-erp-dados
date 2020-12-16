package com.open.erp.openerp.dominio.venda.api;

import com.open.erp.openerp.dominio.venda.api.dto.VendaDto;
import com.open.erp.openerp.dominio.venda.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "venda")
public class VendaController {

    @Autowired
    private VendaService service;

    @PostMapping
    public void efetuarVenda(@RequestBody VendaDto dto) {
        service.efetuarVenda(dto);
    }
}
