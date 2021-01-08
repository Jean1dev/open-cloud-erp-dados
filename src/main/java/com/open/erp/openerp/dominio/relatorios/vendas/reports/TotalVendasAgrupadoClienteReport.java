package com.open.erp.openerp.dominio.relatorios.vendas.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.open.erp.openerp.dominio.relatorios.vendas.dto.VendaAgrupaPorCliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class TotalVendasAgrupadoClienteReport {

    private static final Logger logger = LoggerFactory.getLogger(TotalVendasAgrupadoClienteReport.class);

    public static ByteArrayInputStream gerar(List<VendaAgrupaPorCliente> list) throws DocumentException {
        Document document = new Document();
        logger.info("gerando PDF");

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(60);
        table.setWidths(new int[]{4, 2});

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPCell hcell = new PdfPCell(new Phrase("Cliente", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Total", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        list.forEach(venda -> {
            PdfPCell cell;
            cell = new PdfPCell(new Phrase(venda.getNomeCliente()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(venda.getTotal())));
            cell.setPaddingLeft(5);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
        });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph("Relatorio de vendas agrupado por cliente"));
        document.add(new LineSeparator());
        document.add(table);

        document.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
