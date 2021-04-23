package com.open.erp.openerp.dominio.orcamento.service;

import com.open.erp.openerp.dominio.orcamento.api.dto.TransformarOrcamentoEmVendaDto;
import com.open.erp.openerp.dominio.orcamento.model.Orcamento;
import com.open.erp.openerp.dominio.orcamento.repository.OrcamentoRepository;
import com.open.erp.openerp.dominio.venda.api.dto.VendaDto;
import com.open.erp.openerp.dominio.venda.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository repository;

    @Autowired
    private VendaService vendaService;

    public void transformarOrcamentoEmVenda(TransformarOrcamentoEmVendaDto dto) {
        Orcamento orcamento = repository.findById(dto.getOrcamentoId()).orElseThrow();

        VendaDto vendaDto = VendaDto.builder()
                .cliente(dto.getClienteId())
                .valorRecebido(dto.getValorRecebido())
                .itens(orcamento.getItens().stream().map(itemOrcamento -> VendaDto.ItemVendaDto.builder()
                        .produtoId(itemOrcamento.getProdutoId())
                        .quantidade(itemOrcamento.getQuantidade())
                        .valorUnitario(itemOrcamento.getValorUnitario())
                        .build())
                        .collect(Collectors.toList()))
                .mobile(false)
                .build();

        vendaService.efetuarVenda(vendaDto);
        repository.delete(orcamento);
    }
}
