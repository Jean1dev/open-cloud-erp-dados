package com.open.erp.openerp.dominio.compra.service;

import com.open.erp.openerp.dominio.compra.api.dto.CompraDto;
import com.open.erp.openerp.dominio.compra.model.Compra;
import com.open.erp.openerp.dominio.compra.model.FornecedorAgregado;
import com.open.erp.openerp.dominio.compra.model.ItemCompra;
import com.open.erp.openerp.dominio.compra.repository.CompraRepository;
import com.open.erp.openerp.dominio.estoque.service.EstoqueService;
import com.open.erp.openerp.dominio.fornecedor.model.Fornecedor;
import com.open.erp.openerp.dominio.fornecedor.repositoy.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompraService {

    @Autowired
    private CompraRepository repository;

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private FornecedorRepository fornecedorRepository;

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
                .fornecedor(getFornecedor(dto.getFornecedor()))
                .build());
    }

    private FornecedorAgregado getFornecedor(String fornecedor) {
        if (Objects.isNull(fornecedor))
            return null;

        Fornecedor find = fornecedorRepository.findById(fornecedor).orElse(null);
        assert find != null;
        return FornecedorAgregado.builder()
                .fornecedorId(find.getId())
                .nome(find.getNome())
                .build();
    }
}
