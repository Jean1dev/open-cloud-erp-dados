package com.open.erp.openerp.dominio.venda.service;

import com.open.erp.openerp.dominio.cliente.model.Cliente;
import com.open.erp.openerp.dominio.cliente.repository.ClienteRepository;
import com.open.erp.openerp.dominio.estoque.service.EstoqueService;
import com.open.erp.openerp.dominio.titulosreceber.service.TitulosReceberService;
import com.open.erp.openerp.dominio.venda.api.dto.VendaDto;
import com.open.erp.openerp.dominio.venda.model.ClienteAgregado;
import com.open.erp.openerp.dominio.venda.model.ItemVenda;
import com.open.erp.openerp.dominio.venda.model.Venda;
import com.open.erp.openerp.dominio.venda.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VendaService {

    @Autowired
    private VendaRepository repository;

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TitulosReceberService titulosReceberService;

    public void removerVenda(String vendaId) {
        Venda venda = repository.findById(vendaId).orElseThrow();

        if (venda.getValorRecebido().compareTo(venda.getValorTotal()) != 0)
            removerTitulosAReceber(vendaId);

        venda.getItens().forEach(
                itemVenda -> estoqueService.adicionarNoEstoque(itemVenda.getProdutoId(), itemVenda.getQuantidade())
        );

        repository.delete(venda);
    }

    private void removerTitulosAReceber(String vendaId) {
        titulosReceberService.removerTitulo(titulosReceberService.getTituloByVenda(vendaId));
    }

    public void efetuarVenda(VendaDto dto) {
        List<ItemVenda> itens = dto.getItens()
                .stream()
                .map(ItemVenda::fromDto)
                .collect(Collectors.toList());

        BigDecimal total = itens.stream()
                .map(ItemVenda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        validarOperacao(dto, total);
        itens.forEach(itemCompra -> estoqueService.reduzirNoEstoque(itemCompra.getProdutoId(), itemCompra.getQuantidade()));

        Venda venda = repository.save(Venda.builder()
                .valorTotal(arredondar(total))
                .itens(itens)
                .dataVenda(LocalDate.now())
                .cliente(getCliente(dto.getCliente()))
                .valorRecebido(arredondar(dto.getValorRecebido()))
                .mobile(dto.getMobile())
                .dataLimitePagamento(dto.getDataLimitePagamento())
                .build());

        titulosReceberService.gerarTituloAPartirVenda(venda, dto);
    }

    private BigDecimal arredondar(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private void validarOperacao(VendaDto dto, BigDecimal total) {
        if (total.compareTo(BigDecimal.ZERO) == 0)
            throw new ValidationException("Não é permitido venda com total zerado");

        if (dto.getValorRecebido().compareTo(total) > 0)
            throw new ValidationException("Valor recebido não pode ser superior ao da venda");
    }

    private ClienteAgregado getCliente(String cliente) {
        if (Objects.isNull(cliente))
            return null;

        Cliente clienteReal = clienteRepository.findById(cliente).orElse(null);
        assert clienteReal != null;
        return ClienteAgregado.builder()
                .idCliente(clienteReal.getId())
                .nome(clienteReal.getNome())
                .build();
    }
}
