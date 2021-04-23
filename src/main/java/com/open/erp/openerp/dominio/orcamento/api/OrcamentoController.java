package com.open.erp.openerp.dominio.orcamento.api;

import com.open.erp.openerp.dominio.orcamento.model.Orcamento;
import com.open.erp.openerp.dominio.orcamento.repository.OrcamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(path = "orcamento")
public class OrcamentoController {

    @Autowired
    private OrcamentoRepository repository;

    @PostMapping
    public void saveOrcamento(@RequestBody Orcamento orcamento) {
        orcamento.setDataOrcamento(LocalDate.now());
        repository.save(orcamento);
    }

    @PutMapping(path = "{id}")
    public void alterar(@PathVariable("id") String id, @RequestBody Orcamento orcamento) {
        orcamento.setDataAlteracao(LocalDate.now());
        repository.save(orcamento);
    }

    @GetMapping(path = "paginated")
    public Page<Orcamento> getPage(@RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                   @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset) {
        return repository.findAll(PageRequest.of(offset, limit));
    }

    @GetMapping(path = "{id}")
    public Orcamento getById(@PathVariable("id") String id) {
        return repository.findById(id).orElseThrow();
    }

    @DeleteMapping(path = "{id}")
    public void delete(@PathVariable("id") String id) {
        repository.deleteById(id);
    }
}
