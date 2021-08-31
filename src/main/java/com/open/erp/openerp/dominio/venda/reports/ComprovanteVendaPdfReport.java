package com.open.erp.openerp.dominio.venda.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.open.erp.openerp.dominio.produto.service.ProdutoService;
import com.open.erp.openerp.dominio.venda.model.ClienteAgregado;
import com.open.erp.openerp.dominio.venda.model.ItemVenda;
import com.open.erp.openerp.dominio.venda.model.Venda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ComprovanteVendaPdfReport {

    private static final Logger logger = LoggerFactory.getLogger(ComprovanteVendaPdfReport.class);

    public static ByteArrayInputStream gerarComprovante(Venda venda, ProdutoService produtoService) throws DocumentException, IOException {
        Document document = new Document();
        logger.info("gerando PDF");
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{4, 4, 4});

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
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(itemVenda.getQuantidade())));
            cell.setPaddingLeft(5);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
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
        document.addTitle("Comprovante de venda");

        document.add(tituloReport());
        document.add(new DottedLineSeparator());
        addDadosVendas(document, venda);
        addDadosCliente(document, venda.getCliente());
        addHeaderProdutos(document);
        document.add(table);
        addTotais(document, venda);

        if (Objects.nonNull(venda.getValorAReceber()) && venda.getValorAReceber().compareTo(BigDecimal.ZERO) != 0) {
            document.add(new Paragraph("Valor recebido nessa venda foi de R$:" + venda.getValorRecebido()));
            document.add(new Paragraph("O cliente ainda deve um valor de R$: " + venda.getValorAReceber()));
        }

        if (Objects.nonNull(venda.getDataLimitePagamento())) {
            document.add(new Paragraph("Data limite de pagamento " + venda.getDataLimitePagamento().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))));
        }

        document.close();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static void addHeaderProdutos(Document document) throws DocumentException {
        PdfPTable tableHeader = new PdfPTable(1);
        tableHeader.setWidthPercentage(100);
        tableHeader.setWidths(new int[]{4});
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Phrase header = new Phrase("Produtos", headFont);
        PdfPCell pCell = new PdfPCell(header);
        pCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pCell.setBackgroundColor(getCinza());
        tableHeader.addCell(pCell);
        document.add(tableHeader);
    }

    private static void addDadosVendas(Document document, Venda venda) throws DocumentException, IOException {
        PdfPTable tableHeader = new PdfPTable(1);
        tableHeader.setWidthPercentage(100);
        tableHeader.setWidths(new int[]{4});
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Phrase header = new Phrase("Informações da venda", headFont);
        PdfPCell pCell = new PdfPCell(header);
        pCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pCell.setBackgroundColor(getCinza());
        tableHeader.addCell(pCell);
        document.add(tableHeader);

        PdfPTable tableEmpresa = new PdfPTable(2);
        tableEmpresa.setWidthPercentage(100);
        tableEmpresa.setWidths(new int[]{2, 6});
        Image image1 = Image.getInstance(Objects.requireNonNull(ComprovanteVendaPdfReport.class.getClassLoader().getResource("img/logo.jpg")));
        image1.setAlignment(Element.ALIGN_RIGHT);
        image1.scaleAbsolute(112, 81);
        tableEmpresa.addCell(new PdfPCell(image1));
        tableEmpresa.addCell(new PdfPCell(new Phrase(getInformacoesEmpresa())));
        document.add(tableEmpresa);

        PdfPTable tableVenda = new PdfPTable(2);
        tableVenda.setWidthPercentage(100);
        tableVenda.setWidths(new int[]{6, 2});
        tableVenda.addCell(new PdfPCell(new Phrase("id :" + venda.getId())));
        tableVenda.addCell(new PdfPCell(new Phrase(venda.getDataVenda().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")))));
        document.add(tableVenda);
    }

    private static String getInformacoesEmpresa() {
        return "Globo EPI \n" +
                "Telefone: (48) 99691-0970\n" +
                "CNPJ: 38.496.625/0001-69\n" +
                "IE: 260726486\n" +
                "R SAO JOAO BATISTA, 36 - MORRO GRANDE - SANGAO - SC - 88.717-000";
    }

    private static void addTotais(Document document, Venda venda) throws DocumentException {
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

        PdfPTable tableTotais = new PdfPTable(3);
        tableTotais.setWidthPercentage(100);
        tableTotais.setWidths(new int[]{5, 5, 5});
        BigDecimal totalItens = venda.getItens()
                .stream()
                .map(ItemVenda::getQuantidade)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        tableTotais.addCell(new PdfPCell(new Phrase("Quantidade :" + totalItens)));
        tableTotais.addCell(new PdfPCell(new Phrase("Total Produtos :" + venda.getValorTotal())));
        tableTotais.addCell(new PdfPCell(new Phrase("Total venda :" + venda.getValorTotal())));
        document.add(tableTotais);
    }

    private static void addDadosCliente(Document document, ClienteAgregado cliente) throws DocumentException {
        if (Objects.isNull(cliente))
            return;

        PdfPTable tableHeader = new PdfPTable(1);
        tableHeader.setWidthPercentage(100);
        tableHeader.setWidths(new int[]{4});
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Phrase header = new Phrase("Dados do cliente", headFont);
        PdfPCell pCell = new PdfPCell(header);
        pCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pCell.setBackgroundColor(getCinza());
        tableHeader.addCell(pCell);
        document.add(tableHeader);

        PdfPTable tableCliente = new PdfPTable(2);
        tableCliente.setWidthPercentage(100);
        tableCliente.setWidths(new int[]{6, 2});
        tableCliente.addCell(new PdfPCell(new Phrase(cliente.getNome())));
        tableCliente.addCell(new PdfPCell());
        document.add(tableCliente);
    }

    private static BaseColor getCinza() {
        return WebColors.getRGBColor("#57636e");
    }

    private static Paragraph tituloReport() {
        Font bold = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Paragraph paragraph = new Paragraph("Comprovante de venda", bold);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }
}
