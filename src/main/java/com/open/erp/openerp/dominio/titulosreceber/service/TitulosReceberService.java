package com.open.erp.openerp.dominio.titulosreceber.service;

import com.open.erp.openerp.dominio.titulosreceber.api.dto.TituloComVendaDto;
import com.open.erp.openerp.dominio.titulosreceber.model.TituloAReceber;
import com.open.erp.openerp.dominio.titulosreceber.repository.TituloRebecerRepository;
import com.open.erp.openerp.dominio.venda.api.dto.VendaDto;
import com.open.erp.openerp.dominio.venda.model.Venda;
import com.open.erp.openerp.dominio.venda.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TitulosReceberService {

    @Autowired
    private TituloRebecerRepository repository;

    @Autowired
    private VendaRepository vendaRepository;

    public TituloAReceber getTituloByVenda(String vendaId) {
        return repository.findByVendaId(vendaId);
    }

    public void removerTitulo(TituloAReceber tituloAReceber) {
        repository.delete(tituloAReceber);
    }

    public Set<TituloComVendaDto> getListagemDetalhadaPorCliente(String clienteId) {
        return repository.findAllByCliente(clienteId)
                .stream()
                .map(tituloAReceber -> {
                    Venda venda = vendaRepository.findById(tituloAReceber.getVendaOrigem()).orElseThrow();
                    return TituloComVendaDto.builder()
                            .tituloAReceber(tituloAReceber)
                            .venda(venda)
                            .build();
                }).collect(Collectors.toSet());
    }

    public void gerarTituloAPartirVenda(Venda venda, VendaDto dto) {
        if (verificarSeNaoDeveGerarTitulo(venda))
            return;

        repository.save(TituloAReceber.builder()
                .clienteId(venda.getCliente().getIdCliente())
                .clienteNome(venda.getCliente().getNome())
                .data(LocalDate.now())
                .vendaOrigem(venda.getId())
                .valor(venda.getValorAReceber())
                .quitado(false)
                .dataLimitePagamento(getDataLimitePagamento(dto.getDataLimitePagamento()))
                .build());
    }

    private LocalDate getDataLimitePagamento(LocalDate dataLimitePagamento) {
        if (Objects.isNull(dataLimitePagamento)) {
            dataLimitePagamento = LocalDate.now().plusDays(30);
        }

        return dataLimitePagamento;
    }

    public void quitarTitulo(String id) {
        TituloAReceber tituloAReceber = repository.findById(id).orElseThrow();
        tituloAReceber.setQuitado(true);
        tituloAReceber.setDataQuitado(LocalDate.now());
        repository.save(tituloAReceber);
    }

    private boolean verificarSeNaoDeveGerarTitulo(Venda venda) {
        if (Objects.isNull(venda.getValorAReceber()) || Objects.isNull(venda.getCliente()))
            return true;

        return venda.getValorAReceber().compareTo(BigDecimal.ZERO) == 0;
    }

    public void quitarTodosTitulosDesseCliente(String clienteId) {
        repository.findAllByCliente(clienteId)
                .stream()
                .map(TituloAReceber::getId)
                .forEach(this::quitarTitulo);
    }
}
