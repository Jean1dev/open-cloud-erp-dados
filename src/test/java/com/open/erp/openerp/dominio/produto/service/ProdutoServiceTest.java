package com.open.erp.openerp.dominio.produto.service;

import com.open.erp.openerp.AplicationConfigTest;
import com.open.erp.openerp.dominio.produto.model.Produto;
import com.open.erp.openerp.dominio.produto.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ProdutoServiceTest extends AplicationConfigTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNomeProduto() {
        Produto produto = new Produto();
        produto.setId("1");
        produto.setNome("Test Product");

        when(repository.findById("1")).thenReturn(Optional.of(produto));
        when(repository.findById("2")).thenReturn(Optional.empty());

        String nomeProduto = produtoService.getNomeProduto("1");
        assertEquals("Test Product", nomeProduto);

        nomeProduto = produtoService.getNomeProduto("2");
        assertEquals("Não encontrado", nomeProduto);
    }

    @Test
    void testPeformSearch() {
        Produto produto = new Produto();
        produto.setNome("Test Product");
        Page<Produto> produtoPage = new PageImpl<>(Collections.singletonList(produto));

        when(repository.findAll(any(Pageable.class))).thenReturn(produtoPage);
        when(repository.findByNomeContaining(eq("Test"), any(PageRequest.class))).thenReturn(produtoPage);

        Page<Produto> result = produtoService.peformSearch("Test", 10, 0);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getNome());

        result = produtoService.peformSearch("", 10, 0);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().get(0).getNome());
    }

}