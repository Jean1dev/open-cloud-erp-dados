package com.open.erp.openerp.dominio.compra.api;

import com.open.erp.openerp.dominio.compra.api.dto.CompraDto;
import com.open.erp.openerp.dominio.compra.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "compra")
public class CompraController {

    @Autowired
    private CompraService service;

    @PostMapping
    public void efetuarCompra(@RequestBody CompraDto dto) {
        service.efetuarCompra(dto);
    }
}
