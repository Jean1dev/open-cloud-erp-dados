package com.open.erp.openerp.dominio.cliente.repository;

import com.open.erp.openerp.dominio.cliente.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClienteRepository extends MongoRepository<Cliente, String> {
}
