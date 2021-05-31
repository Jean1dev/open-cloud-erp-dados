package com.open.erp.openerp.dominio.venda.repository;

import com.open.erp.openerp.dominio.venda.model.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VendaRepository extends MongoRepository<Venda, String>, PagingAndSortingRepository<Venda, String> {

    Page<Venda> findAllByOrderByDataVendaDesc(Pageable pageable);
}
