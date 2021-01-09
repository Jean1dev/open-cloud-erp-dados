package com.open.erp.openerp.dominio.relatorios.vendas;

import com.itextpdf.text.DocumentException;
import com.open.erp.openerp.dominio.relatorios.vendas.reports.VendasMensaisReport;
import com.open.erp.openerp.dominio.venda.model.Venda;
import com.open.erp.openerp.dominio.venda.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendasMensais {

    @Autowired
    private VendaRepository repository;

    public ByteArrayInputStream gerar(YearMonth mesAno) throws DocumentException {
        List<Venda> vendas = repository.findAll();

        List<Venda> collect = vendas.stream()
                .filter(venda -> YearMonth.from(venda.getDataVenda()).equals(mesAno))
                .collect(Collectors.toList());

        if (collect.isEmpty())
            return null;

        YearMonth yearMonth = YearMonth.from(collect.get(0).getDataVenda());
        BigDecimal total = collect.stream()
                .map(Venda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return VendasMensaisReport.gerar(collect, yearMonth, total);
    }
}
