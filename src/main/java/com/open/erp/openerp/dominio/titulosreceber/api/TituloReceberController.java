package com.open.erp.openerp.dominio.titulosreceber.api;

import com.open.erp.openerp.dominio.titulosreceber.model.TituloAReceber;
import com.open.erp.openerp.dominio.titulosreceber.repository.TituloRebecerRepository;
import com.open.erp.openerp.dominio.titulosreceber.service.TitulosReceberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "titulo-receber")
public class TituloReceberController {

    @Autowired
    private TituloRebecerRepository repository;

    @Autowired
    private TitulosReceberService service;

    @GetMapping(path = "paginated")
    public Page<TituloAReceber> getPage(@RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                        @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset) {
        return repository.findAll(PageRequest.of(offset, limit));
    }

    @PutMapping(path = "{id}")
    public void quitarTitulo(@PathVariable("id") String id) {
        service.quitarTitulo(id);
    }
}
