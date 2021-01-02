package com.open.erp.openerp.dominio.produto.repository;

import com.open.erp.openerp.dominio.produto.model.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProdutoRepository extends MongoRepository<Produto, String>, PagingAndSortingRepository<Produto, String> {
}
