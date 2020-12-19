package com.open.erp.openerp.dominio.compra.service;

import com.open.erp.openerp.dominio.compra.api.dto.CompraDto;
import com.open.erp.openerp.dominio.compra.model.Compra;
import com.open.erp.openerp.dominio.compra.model.ItemCompra;
import com.open.erp.openerp.dominio.compra.repository.CompraRepository;
import com.open.erp.openerp.dominio.estoque.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompraService {

    @Autowired
    private CompraRepository repository;

    @Autowired
    private EstoqueService estoqueService;

    public void efetuarCompra(CompraDto dto) {
        List<ItemCompra> itemCompras = dto.getItens()
                .stream()
                .map(ItemCompra::fromDto)
                .collect(Collectors.toList());

        BigDecimal total = itemCompras.stream()
                .map(itemCompra -> itemCompra.getValorTotal())
                .reduce(BigDecimal.ZERO, (somatorio, valorUnitario) -> somatorio.add(valorUnitario));

        itemCompras.forEach(itemCompra -> estoqueService.adicionarNoEstoque(itemCompra.getProdutoId(), itemCompra.getQuantidade()));

        repository.save(Compra.builder()
                .valorTotal(total)
                .itens(itemCompras)
                .dataCompra(LocalDate.now())
                .build());
    }
}
