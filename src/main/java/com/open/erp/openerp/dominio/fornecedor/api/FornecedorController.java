package com.open.erp.openerp.dominio.fornecedor.api;

import com.open.erp.openerp.dominio.fornecedor.model.Fornecedor;
import com.open.erp.openerp.dominio.fornecedor.repositoy.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "fornecedor")
public class FornecedorController {

    @Autowired
    private FornecedorRepository repository;

    @PostMapping
    public void criar(@Valid @RequestBody Fornecedor fornecedor) {
        repository.save(fornecedor);
    }

    @GetMapping
    public List<Fornecedor> findAll() {
        return repository.findAll();
    }

    @GetMapping(path = "paginated")
    public Page<Fornecedor> getPage(@RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                    @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset) {
        return repository.findAll(PageRequest.of(offset, limit));
    }
}
