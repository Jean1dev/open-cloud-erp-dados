package com.open.erp.openerp.dominio.relatorios.vendas;

import com.itextpdf.text.DocumentException;
import com.open.erp.openerp.dominio.relatorios.vendas.dto.VendaAgrupaPorCliente;
import com.open.erp.openerp.dominio.relatorios.vendas.reports.TotalVendasAgrupadoClienteReport;
import com.open.erp.openerp.dominio.venda.model.ClienteAgregado;
import com.open.erp.openerp.dominio.venda.model.Venda;
import com.open.erp.openerp.dominio.venda.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TotalVendasAgrupadoCliente {

    @Autowired
    private VendaRepository repository;

    public ByteArrayInputStream gerar() throws DocumentException {
        Map<String, List<Venda>> agregadoListMap = repository.findAll()
                .stream()
                .filter(venda -> Objects.nonNull(venda.getCliente()))
                .collect(Collectors.groupingBy(venda -> venda.getCliente().getNome()));

        List<VendaAgrupaPorCliente> agrupados = new ArrayList<>();
        agregadoListMap.forEach((clienteAgregado, vendas) -> {
            BigDecimal total = vendas.stream()
                    .map(Venda::getValorTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            agrupados.add(VendaAgrupaPorCliente.builder()
                    .nomeCliente(clienteAgregado)
                    .total(total)
                    .build());
        });

        return TotalVendasAgrupadoClienteReport.gerar(agrupados);
    }
}
