package com.open.erp.openerp.dominio.cliente.api;

import com.open.erp.openerp.dominio.cliente.model.Cliente;
import com.open.erp.openerp.dominio.cliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @PostMapping
    public void criar(@RequestBody Cliente cliente) {
        repository.save(cliente);
    }

    @GetMapping
    public List<Cliente> findAll() {
        return repository.findAll();
    }
}
