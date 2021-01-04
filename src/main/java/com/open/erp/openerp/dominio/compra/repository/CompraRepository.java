package com.open.erp.openerp.dominio.compra.repository;

import com.open.erp.openerp.dominio.compra.model.Compra;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompraRepository extends MongoRepository<Compra, String>, PagingAndSortingRepository<Compra, String> {
}
