package com.open.erp.openerp.dominio.venda.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.open.erp.openerp.dominio.produto.service.ProdutoService;
import com.open.erp.openerp.dominio.venda.model.Venda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ComprovanteVendaPdfReport {

    private static final Logger logger = LoggerFactory.getLogger(ComprovanteVendaPdfReport.class);

    public static ByteArrayInputStream gerarComprovante(Venda venda, ProdutoService produtoService) throws DocumentException {
        Document document = new Document();
        logger.info("gerando PDF");
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(60);
        table.setWidths(new int[]{4, 2, 2});

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPCell hcell = new PdfPCell(new Phrase("Produto", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Quantidade", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Valor", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        venda.getItens().forEach(itemVenda -> {
            PdfPCell cell;
            cell = new PdfPCell(new Phrase(produtoService.getNomeProduto(itemVenda.getProdutoId())));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(itemVenda.getQuantidade())));
            cell.setPaddingLeft(5);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(itemVenda.getValorUnitario())));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPaddingRight(5);
            table.addCell(cell);
        });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph("Comprovante de venda"));
        document.add(new Paragraph(venda.getDataVenda().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))));
        if (Objects.nonNull(venda.getCliente()))
            document.add(new Paragraph("Cliente: " + venda.getCliente().getNome()));

        document.add(new Paragraph());
        document.add(new Paragraph());
        document.add(new Paragraph());

        document.add(table);
        document.add(new Paragraph("Valor total R$:" + venda.getValorTotal()));

        document.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
