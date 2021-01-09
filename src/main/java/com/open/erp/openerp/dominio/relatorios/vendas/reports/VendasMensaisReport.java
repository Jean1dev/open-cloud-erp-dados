package com.open.erp.openerp.dominio.relatorios.vendas.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.open.erp.openerp.dominio.venda.model.Venda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class VendasMensaisReport {

    private static final Logger logger = LoggerFactory.getLogger(VendasMensaisReport.class);

    public static ByteArrayInputStream gerar(List<Venda> vendas, YearMonth mesAno, BigDecimal total) throws DocumentException {
        Document document = new Document();
        logger.info("gerando PDF");

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(60);
        table.setWidths(new int[]{4, 2, 2});

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPCell hcell = new PdfPCell(new Phrase("Cliente", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Data", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Valor", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        vendas.forEach(venda -> {
            PdfPCell cell;
            String cliente = Objects.nonNull(venda.getCliente()) ? venda.getCliente().getNome() : " -- ";
            cell = new PdfPCell(new Phrase(cliente));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(venda.getDataVenda().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))));
            cell.setPaddingLeft(5);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(venda.getValorTotal())));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPaddingRight(5);
            table.addCell(cell);
        });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph("Relatorio de vendas mensal"));
        int mes = mesAno.getMonthValue();
        int ano = mesAno.getYear();
        document.add(new Paragraph("mes " + mes + " ano " + ano));
        document.add(new Paragraph("Valor total R$:" + total));
        document.add(table);

        document.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
