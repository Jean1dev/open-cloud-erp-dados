package com.open.erp.openerp.dominio.titulosreceber.repository;

import com.open.erp.openerp.dominio.titulosreceber.model.TituloAReceber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TituloRebecerRepository extends MongoRepository<TituloAReceber, String>, PagingAndSortingRepository<TituloAReceber, String> {

    @Query("{ 'quitado': false }")
    Page<TituloAReceber> findAll(Pageable pageable);
}
