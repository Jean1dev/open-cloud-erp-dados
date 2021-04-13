package com.open.erp.openerp.dominio.relatorios.api;

import com.itextpdf.text.DocumentException;
import com.open.erp.openerp.dominio.relatorios.TipoRelatorio;
import com.open.erp.openerp.dominio.relatorios.tituloreceber.reports.DetalhamentoReceberClienteReport;
import com.open.erp.openerp.dominio.relatorios.vendas.TotalVendasAgrupadoCliente;
import com.open.erp.openerp.dominio.relatorios.vendas.VendasMensais;
import com.open.erp.openerp.dominio.titulosreceber.api.dto.TituloComVendaDto;
import com.open.erp.openerp.dominio.titulosreceber.service.TitulosReceberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "relatorios")
public class RelatorioController {

    @Autowired
    private VendasMensais vendasMensais;

    @Autowired
    private TotalVendasAgrupadoCliente agrupadoCliente;

    @Autowired
    private TitulosReceberService titulosReceberService;

    @GetMapping
    public List<TipoRelatorio> getTiposRelatorios() {
        return Arrays.asList(TipoRelatorio.VENDAS_MENSAL, TipoRelatorio.VENDAS_AGRUPADO_POR_CLIENTE);
    }

    @GetMapping(path = "detalhamento", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> detalhamentoTitulosPorCliente(@RequestParam(value = "clienteId") String clienteId) throws DocumentException {
        Set<TituloComVendaDto> dtos = titulosReceberService.getListagemDetalhadaPorCliente(clienteId);
        if (dtos.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "nenhum titulo foi encontrado para esse cliente");

        ByteArrayInputStream report = DetalhamentoReceberClienteReport.gerar(dtos);
        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=detalhamento-titulos.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(report));
    }

    @GetMapping(path = "vendas-mensais", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> vendasMensais(@RequestParam("mes") Integer mes, @RequestParam("ano") Integer ano) throws DocumentException {
        ByteArrayInputStream report = vendasMensais.gerar(YearMonth.of(ano, mes));

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=vendasmensais.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(report));
    }

    @GetMapping(path = "clientes-agrupados", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> clienteAgrupado() throws DocumentException {
        ByteArrayInputStream report = agrupadoCliente.gerar();

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=vendasmensais.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(report));
    }
}
