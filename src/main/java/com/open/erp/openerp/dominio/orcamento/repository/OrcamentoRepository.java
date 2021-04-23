package com.open.erp.openerp.dominio.orcamento.repository;

import com.open.erp.openerp.dominio.orcamento.model.Orcamento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrcamentoRepository extends MongoRepository<Orcamento, String>, PagingAndSortingRepository<Orcamento, String> {
}
