package com.open.erp.openerp.dominio.cliente.api;

import com.open.erp.openerp.dominio.cliente.model.Cliente;
import com.open.erp.openerp.dominio.cliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @PostMapping
    public void criar(@Valid @RequestBody Cliente cliente) {
        repository.save(cliente);
    }

    @GetMapping
    public List<Cliente> findAll() {
        return repository.findAll();
    }

    @GetMapping(path = "paginated")
    public Page<Cliente> getPage(@RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                 @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset) {
        return repository.findAll(PageRequest.of(offset, limit));
    }
}
