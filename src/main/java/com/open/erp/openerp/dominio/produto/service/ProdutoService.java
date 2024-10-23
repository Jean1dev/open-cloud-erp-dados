package com.open.erp.openerp.dominio.produto.service;

import com.open.erp.openerp.dominio.produto.model.Produto;
import com.open.erp.openerp.dominio.produto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public String getNomeProduto(String idProduto) {
        Optional<Produto> optionalProduto = repository.findById(idProduto);

        return optionalProduto.isPresent() ? optionalProduto.get().getNome() : "Não encontrado";
    }

    public Page<Produto> peformSearch(String nome, Integer limit, Integer offset) {
        if (nome == null || nome.isEmpty()) {
            return repository.findAll(PageRequest.of(offset, limit));
        }
        return repository.findByNomeContaining(nome, PageRequest.of(offset, limit));
    }
}
