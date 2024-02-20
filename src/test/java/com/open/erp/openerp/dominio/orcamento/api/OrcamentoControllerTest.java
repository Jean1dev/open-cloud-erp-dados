package com.open.erp.openerp.dominio.orcamento.api;

import com.open.erp.openerp.AplicationConfigTest;
import com.open.erp.openerp.dominio.orcamento.model.ItemOrcamento;
import com.open.erp.openerp.dominio.orcamento.model.Orcamento;
import com.open.erp.openerp.dominio.orcamento.repository.OrcamentoRepository;
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
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class OrcamentoControllerTest extends AplicationConfigTest {

    @MockBean
    private OrcamentoRepository orcamentoRepository;

    @MockBean
    private ProdutoService produtoService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ThreadPoolTaskExecutor executor;

    private static final String PATH = "/orcamento";

    @Test
    @DisplayName("Deve gerar um relatorio e enviar para api do whatsapp")
    void enviarComprovante() throws Exception {
        var orcamento = umOrcamentoMock();

        when(orcamentoRepository.findById(orcamento.getId())).thenReturn(Optional.of(orcamento));
        when(produtoService.getNomeProduto(anyString())).thenReturn("name produto mock");

        mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/{id}/enviar-comprovante", orcamento.getId())
                        .param("fone", "48998457797")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        executor.getThreadPoolExecutor().awaitTermination(20, TimeUnit.SECONDS);
    }

    private Orcamento umOrcamentoMock() {
        return Orcamento.builder()
                .id("ID mock")
                .dataOrcamento(LocalDate.now())
                .valorTotal(BigDecimal.TEN)
                .itens(Set.of(umItem(), umItem()))
                .build();
    }

    private ItemOrcamento umItem() {
        return ItemOrcamento.builder()
                .produtoId("ProdutoId")
                .quantidade(BigDecimal.ONE)
                .valorTotal(BigDecimal.ONE)
                .valorUnitario(BigDecimal.TEN)
                .build();
    }
}