package com.open.erp.openerp.dominio.titulosreceber.service;

import com.open.erp.openerp.dominio.titulosreceber.model.TituloAReceber;
import com.open.erp.openerp.dominio.titulosreceber.repository.TituloRebecerRepository;
import com.open.erp.openerp.dominio.venda.model.Venda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class TitulosReceberService {

    @Autowired
    private TituloRebecerRepository repository;

    public void gerarTituloAPartirVenda(Venda venda) {
        if (verificarSeNaoDeveGerarTitulo(venda))
            return;

        repository.save(TituloAReceber.builder()
                .clienteId(venda.getCliente().getIdCliente())
                .clienteNome(venda.getCliente().getNome())
                .data(LocalDate.now())
                .vendaOrigem(venda.getId())
                .valor(venda.getValorAReceber())
                .quitado(false)
                .build());
    }

    public void quitarTitulo(String id) {
        TituloAReceber tituloAReceber = repository.findById(id).orElseThrow();
        tituloAReceber.setQuitado(true);
        tituloAReceber.setDataQuitado(LocalDate.now());
        repository.save(tituloAReceber);
    }

    private boolean verificarSeNaoDeveGerarTitulo(Venda venda) {
        if (Objects.isNull(venda.getValorAReceber())  || Objects.isNull(venda.getCliente()))
            return true;

        return venda.getValorAReceber().compareTo(BigDecimal.ZERO) == 0;
    }
}
