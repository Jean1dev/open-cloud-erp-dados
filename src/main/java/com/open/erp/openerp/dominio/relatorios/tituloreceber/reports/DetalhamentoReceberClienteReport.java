package com.open.erp.openerp.dominio.relatorios.tituloreceber.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.open.erp.openerp.dominio.titulosreceber.api.dto.TituloComVendaDto;
import com.open.erp.openerp.dominio.titulosreceber.model.TituloAReceber;
import com.open.erp.openerp.dominio.venda.model.Venda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class DetalhamentoReceberClienteReport {

    private static final Logger logger = LoggerFactory.getLogger(DetalhamentoReceberClienteReport.class);

    public static ByteArrayInputStream gerar(Set<TituloComVendaDto> registros) throws DocumentException {
        Document document = new Document();
        logger.info("gerando PDF");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        addCabecalho(document);
        addInfoCliente(document, registros.stream().findFirst().get().getTituloAReceber());
        addDivisorVendas(document);
        addHistoricoVendas(document, registros);
        addTotais(document, registros);

        document.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static void addTotais(Document document, Set<TituloComVendaDto> registros) throws DocumentException {
        PdfPTable tableHeader = new PdfPTable(1);
        tableHeader.setWidthPercentage(100);
        tableHeader.setWidths(new int[]{4});
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Phrase header = new Phrase("Totais", headFont);
        PdfPCell pCell = new PdfPCell(header);
        pCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pCell.setBackgroundColor(getCinza());
        tableHeader.addCell(pCell);
        document.add(tableHeader);

        PdfPTable tableTotais = new PdfPTable(2);
        tableTotais.setWidthPercentage(100);
        tableTotais.setWidths(new int[]{5, 5});
        BigDecimal totalVendas = registros
                .stream()
                .map(TituloComVendaDto::getVenda)
                .map(Venda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDivida = registros.stream()
                .map(TituloComVendaDto::getTituloAReceber)
                .map(TituloAReceber::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        tableTotais.addCell(new PdfPCell(new Phrase("Total Vendas :" + totalVendas)));
        tableTotais.addCell(new PdfPCell(new Phrase("Total dividas :" + totalDivida)));
        document.add(tableTotais);
    }

    private static void addHistoricoVendas(Document document, Set<TituloComVendaDto> registros) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{4, 4, 4, 4});
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPCell hcell = new PdfPCell(new Phrase("Data venda", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Data limite para pagamento", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Total Venda", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Total divida", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);
        registros.forEach(dto -> {
            PdfPCell cell;
            cell = new PdfPCell(new Phrase(dto.getVenda().getDataVenda().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(dto.getTituloAReceber().getDataLimitePagamento().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(dto.getVenda().getValorTotal())));
            cell.setPaddingLeft(5);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(dto.getTituloAReceber().getValor())));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPaddingRight(5);
            table.addCell(cell);
        });

        document.add(table);
    }

    private static void addDivisorVendas(Document document) throws DocumentException {
        PdfPTable tableHeader = new PdfPTable(1);
        tableHeader.setWidthPercentage(100);
        tableHeader.setWidths(new int[]{4});
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Phrase header = new Phrase("Historico de vendas", headFont);
        PdfPCell pCell = new PdfPCell(header);
        pCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pCell.setBackgroundColor(getCinza());
        tableHeader.addCell(pCell);
        document.add(tableHeader);
    }

    private static void addInfoCliente(Document document, TituloAReceber tituloAReceber) throws DocumentException {
        PdfPTable tableHeader = new PdfPTable(2);
        tableHeader.setWidthPercentage(100);
        tableHeader.setWidths(new int[]{2, 4});
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Phrase header = new Phrase("Cliente", headFont);
        PdfPCell pCell = new PdfPCell(header);
        pCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pCell.setBackgroundColor(getCinza());
        tableHeader.addCell(pCell);
        tableHeader.addCell(new PdfPCell(new Phrase(tituloAReceber.getClienteNome())));
        document.add(tableHeader);
    }

    private static void addCabecalho(Document document) throws DocumentException {
        Font bold = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Paragraph paragraph = new Paragraph("Impressão de pendencias do cliente", bold);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
    }

    private static BaseColor getCinza() {
        return WebColors.getRGBColor("#57636e");
    }
}
