package com.open.erp.openerp.dominio.venda.api;

import com.itextpdf.text.DocumentException;
import com.open.erp.openerp.dominio.produto.model.Produto;
import com.open.erp.openerp.dominio.produto.service.ProdutoService;
import com.open.erp.openerp.dominio.venda.api.dto.VendaDto;
import com.open.erp.openerp.dominio.venda.model.Venda;
import com.open.erp.openerp.dominio.venda.reports.ComprovanteVendaPdfReport;
import com.open.erp.openerp.dominio.venda.repository.VendaRepository;
import com.open.erp.openerp.dominio.venda.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "venda")
public class VendaController {

    @Autowired
    private VendaService service;

    @Autowired
    private VendaRepository repository;

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public void efetuarVenda(@RequestBody VendaDto dto) {
        service.efetuarVenda(dto);
    }

    @GetMapping
    public List<Venda> findAll() {
        return repository.findAll();
    }

    @GetMapping(path = "paginated")
    public Page<Venda> getPage(@RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                               @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset) {
        return repository.findAllByOrderByDataVendaDesc(PageRequest.of(offset, limit));
    }

    @GetMapping(path = "gerar-comprovante", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> gerarComprovante(@RequestParam(value = "id") String id) throws DocumentException, IOException {
        Venda venda = repository.findById(id).orElseThrow();

        ByteArrayInputStream comprovante = ComprovanteVendaPdfReport.gerarComprovante(venda, produtoService);
        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=comprovante.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(comprovante));
    }

    @DeleteMapping(path = "{id}")
    public void remove(@PathVariable("id") String vendaId) {
        service.removerVenda(vendaId);
    }
}
