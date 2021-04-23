package com.open.erp.openerp.dominio.orcamento.api;

import com.itextpdf.text.DocumentException;
import com.open.erp.openerp.dominio.orcamento.api.dto.TransformarOrcamentoEmVendaDto;
import com.open.erp.openerp.dominio.orcamento.model.Orcamento;
import com.open.erp.openerp.dominio.orcamento.reports.ComprovanteOrcamentoPdfReport;
import com.open.erp.openerp.dominio.orcamento.repository.OrcamentoRepository;
import com.open.erp.openerp.dominio.orcamento.service.OrcamentoService;
import com.open.erp.openerp.dominio.produto.service.ProdutoService;
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
import java.time.LocalDate;

@RestController
@RequestMapping(path = "orcamento")
public class OrcamentoController {

    @Autowired
    private OrcamentoRepository repository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private OrcamentoService service;

    @PostMapping
    public Orcamento saveOrcamento(@RequestBody Orcamento orcamento) {
        orcamento.setDataOrcamento(LocalDate.now());
        return repository.save(orcamento);
    }

    @PostMapping(path = "transformar")
    public void transformarEmVenda(@RequestBody TransformarOrcamentoEmVendaDto dto) {
        service.transformarOrcamentoEmVenda(dto);
    }

    @PutMapping(path = "{id}")
    public void alterar(@PathVariable("id") String id, @RequestBody Orcamento orcamento) {
        orcamento.setDataAlteracao(LocalDate.now());
        repository.save(orcamento);
    }

    @GetMapping(path = "paginated")
    public Page<Orcamento> getPage(@RequestParam(value = "limit", defaultValue = "10", required = false) Integer limit,
                                   @RequestParam(value = "offset", defaultValue = "0", required = false) Integer offset) {
        return repository.findAll(PageRequest.of(offset, limit));
    }

    @GetMapping(path = "{id}")
    public Orcamento getById(@PathVariable("id") String id) {
        return repository.findById(id).orElseThrow();
    }

    @DeleteMapping(path = "{id}")
    public void delete(@PathVariable("id") String id) {
        repository.deleteById(id);
    }

    @GetMapping(path = "gerar-comprovante", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> gerarComprovante(@RequestParam(value = "id") String id) throws DocumentException, IOException {
        Orcamento orcamento = repository.findById(id).orElseThrow();

        ByteArrayInputStream comprovante = ComprovanteOrcamentoPdfReport.gerarComprovante(orcamento, produtoService);
        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=orcamento.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(comprovante));
    }
}
