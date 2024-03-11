package com.open.erp.openerp.dominio.compra.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.open.erp.openerp.AplicationConfigTest;
import com.open.erp.openerp.dominio.compra.api.dto.CompraDto;
import com.open.erp.openerp.dominio.compra.model.Compra;
import com.open.erp.openerp.dominio.compra.model.FornecedorAgregado;
import com.open.erp.openerp.dominio.compra.model.ItemCompra;
import com.open.erp.openerp.dominio.compra.repository.CompraRepository;
import com.open.erp.openerp.dominio.estoque.service.EstoqueService;
import com.open.erp.openerp.dominio.fornecedor.model.Fornecedor;
import com.open.erp.openerp.dominio.fornecedor.repositoy.FornecedorRepository;
import com.open.erp.openerp.dominio.produto.service.ProdutoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Compras test")
class CompraControllerTest extends AplicationConfigTest {
    @MockBean
    private EstoqueService estoqueService;
    @MockBean
    private CompraRepository compraRepository;
    @MockBean
    private FornecedorRepository fornecedorRepository;
    @MockBean
    private ProdutoService produtoService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ThreadPoolTaskExecutor executor;

    private static final String PATH = "/compra";

    @Test
    @DisplayName("Deve criar uma compra e enviar o comprovante")
    void efetuarCompra() throws Exception {
        CompraDto compraDto = criarCompraDto();
        Fornecedor fornecedor = Fornecedor.builder()
                .telefone("48 998457797")
                .build();

        when(fornecedorRepository.findById(anyString())).thenReturn(Optional.of(fornecedor));
        when(produtoService.getNomeProduto(anyString())).thenReturn("Produto mock");

        Compra compra = Compra.builder()
                .fornecedor(FornecedorAgregado.builder().fornecedorId("any").build())
                .itens(Collections.singletonList(ItemCompra.builder()
                        .quantidade(BigDecimal.ONE)
                        .valorTotal(BigDecimal.TEN)
                        .produtoId("any")
                        .build()))
                .valorTotal(BigDecimal.TEN)
                .build();

        when(compraRepository.save(any())).thenReturn(compra);

        mockMvc.perform(MockMvcRequestBuilders.post(PATH)
                        .content(new ObjectMapper().writeValueAsString(compraDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        executor.getThreadPoolExecutor().awaitTermination(20, TimeUnit.SECONDS);
    }

    public static CompraDto criarCompraDto() {
        CompraDto compraDto = new CompraDto();
        compraDto.setFornecedor("fornecedor");
        compraDto.setEnviarComprovante(true);
        var itens = List.of(new CompraDto.ItemCompraDto("id", BigDecimal.ONE, BigDecimal.TEN));
        compraDto.setItens(itens);
        return compraDto;
    }
}