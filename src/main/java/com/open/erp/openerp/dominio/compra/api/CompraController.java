package com.open.erp.openerp.dominio.compra.api;

import com.open.erp.openerp.dominio.compra.api.dto.CompraDto;
import com.open.erp.openerp.dominio.compra.model.Compra;
import com.open.erp.openerp.dominio.compra.repository.CompraRepository;
import com.open.erp.openerp.dominio.compra.service.CompraService;
import com.open.erp.openerp.dominio.produto.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "compra")
public class CompraController {

    @Autowired
    private CompraService service;

    @Autowired
    private CompraRepository repository;

    @PostMapping
    public void efetuarCompra(@RequestBody CompraDto dto) {
        service.efetuarCompra(dto);
    }

    @GetMapping
    public List<Compra> findAll() {
        return repository.findAll();
    }

    @GetMapping(path = "paginated")
    public Page<Compra> getPage(@RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                 @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset) {
        return repository.findAll(PageRequest.of(offset, limit));
    }
}
