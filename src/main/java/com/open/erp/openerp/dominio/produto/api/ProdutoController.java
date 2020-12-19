package com.open.erp.openerp.dominio.produto.api;

import com.open.erp.openerp.dominio.produto.model.Produto;
import com.open.erp.openerp.dominio.produto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "produto")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    @PostMapping
    public void criar(@RequestBody Produto produto) {
        repository.save(produto);
    }

    @GetMapping
    public List<Produto> findAll() {
        return repository.findAll();
    }
}
