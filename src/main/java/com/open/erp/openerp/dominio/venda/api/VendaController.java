package com.open.erp.openerp.dominio.venda.api;

import com.open.erp.openerp.dominio.produto.model.Produto;
import com.open.erp.openerp.dominio.venda.api.dto.VendaDto;
import com.open.erp.openerp.dominio.venda.model.Venda;
import com.open.erp.openerp.dominio.venda.repository.VendaRepository;
import com.open.erp.openerp.dominio.venda.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "venda")
public class VendaController {

    @Autowired
    private VendaService service;

    @Autowired
    private VendaRepository repository;

    @PostMapping
    public void efetuarVenda(@RequestBody VendaDto dto) {
        service.efetuarVenda(dto);
    }

    @GetMapping
    public List<Venda> findAll() {
        return repository.findAll();
    }

    @GetMapping(path = "paginated")
    public Page<Venda> getPage(@RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                 @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset) {
        return repository.findAll(PageRequest.of(offset, limit));
    }
}
