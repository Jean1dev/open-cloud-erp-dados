package com.open.erp.openerp.dominio.fornecedor.repositoy;

import com.open.erp.openerp.dominio.fornecedor.model.Fornecedor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FornecedorRepository extends MongoRepository<Fornecedor, String>, PagingAndSortingRepository<Fornecedor, String> {
}
