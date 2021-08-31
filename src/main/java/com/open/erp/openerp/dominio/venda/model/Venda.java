package com.open.erp.openerp.dominio.venda.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@Document
public class Venda implements Serializable {

    @Id
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataVenda;

    @Positive
    private BigDecimal valorTotal;

    private BigDecimal valorRecebido;

    private BigDecimal valorAReceber;

    private List<ItemVenda> itens;

    private ClienteAgregado cliente;

    private Boolean mobile;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataLimitePagamento;

    @Builder
    private Venda(String id,
                  LocalDate dataVenda,
                  BigDecimal valorTotal,
                  BigDecimal valorRecebido,
                  BigDecimal valorAReceber,
                  List<ItemVenda> itens,
                  ClienteAgregado cliente,
                  Boolean mobile,
                  LocalDate dataLimitePagamento) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
        this.valorRecebido = valorRecebido;
        this.valorAReceber = valorAReceber;
        this.itens = itens;
        this.cliente = cliente;
        this.mobile = mobile;
        this.valorAReceber = calcularValorAReceber();
        this.dataLimitePagamento = dataLimitePagamento;
    }

    private BigDecimal calcularValorAReceber() {
        if (Objects.isNull(this.valorRecebido))
            return null;

        return this.valorTotal.subtract(valorRecebido);
    }
}
