package com.open.erp.openerp.dominio.estoque.service;

import com.open.erp.openerp.dominio.produto.model.Produto;
import com.open.erp.openerp.dominio.produto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class EstoqueService {

    @Autowired
    private ProdutoRepository repository;

    public void adicionarNoEstoque(String produtoId, BigDecimal qtd) {
        Produto produto = repository.findById(produtoId).orElseThrow();

        BigDecimal estoqueAtual = produto.getEstoque();

        if (Objects.isNull(estoqueAtual)) {
            estoqueAtual = BigDecimal.ZERO;
        }

        produto.setEstoque(estoqueAtual.add(qtd));

        repository.save(produto);
    }

    public void reduzirNoEstoque(String produtoId, BigDecimal qtd) {
        Produto produto = repository.findById(produtoId).orElseThrow();

        BigDecimal estoqueAtual = produto.getEstoque();

        if (Objects.isNull(estoqueAtual)) {
            estoqueAtual = BigDecimal.ZERO;
        }

        produto.setEstoque(estoqueAtual.subtract(qtd));

        repository.save(produto);
    }
}
