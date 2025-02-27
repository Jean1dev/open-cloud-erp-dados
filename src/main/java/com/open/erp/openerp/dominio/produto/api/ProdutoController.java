package com.open.erp.openerp.dominio.produto.api;

import com.open.erp.openerp.dominio.produto.model.Produto;
import com.open.erp.openerp.dominio.produto.repository.ProdutoRepository;
import com.open.erp.openerp.dominio.produto.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "produto")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;
    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public void criar(@Valid @RequestBody Produto produto) {
        repository.save(produto);
    }

    @GetMapping
    public List<Produto> findAll() {
        return repository.findAll();
    }

    @GetMapping(path = "paginated")
    public Page<Produto> getPage(@RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                 @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset,
                                 ProdutosSearchCriteria searchCriteria) {
        return produtoService.peformSearch(searchCriteria.nome(), limit, offset);
    }

    @PutMapping(path = "{id}")
    public void alterar(@PathVariable("id") String id, @RequestBody Produto produto) {
        Produto p = repository.findById(id).orElseThrow();
        p.setCa(produto.getCa());
        p.setValorVenda(produto.getValorVenda());
        p.setNome(produto.getNome());
        p.setEstoque(produto.getEstoque());
        p.setValorCompra(produto.getValorCompra());
        repository.save(p);
    }
}
