package com.open.erp.openerp.dominio.venda.repository;

import com.open.erp.openerp.dominio.venda.model.Venda;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VendaRepository extends MongoRepository<Venda, String> {
}
