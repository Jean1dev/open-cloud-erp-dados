package com.open.erp.openerp.commons;

import com.open.erp.openerp.dominio.cliente.model.Cliente;
import com.open.erp.openerp.dominio.cliente.repository.ClienteRepository;
import com.open.erp.openerp.dominio.compra.model.Compra;
import com.open.erp.openerp.dominio.fornecedor.model.Fornecedor;
import com.open.erp.openerp.dominio.fornecedor.repositoy.FornecedorRepository;
import com.open.erp.openerp.dominio.produto.service.ProdutoService;
import com.open.erp.openerp.dominio.venda.model.Venda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.JsonNode;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@EnableAsync
public class ComprovantesWhatsappService {
    Logger log = LoggerFactory.getLogger(ComprovantesWhatsappService.class);

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private FornecedorRepository fornecedorRepository;
    @Autowired
    private ProdutoService produtoService;

    @Value("${whats-api.token}")
    private String WHATS_TOKEN;
    @Value("${whats-api.session}")
    private String WHATS_SESSION;

    private final RestClient restClient = RestClient.create();

    @Async
    public void enviar(Compra compra) {
        Optional<Fornecedor> optFornecedor = fornecedorRepository.findById(compra.getFornecedor().getFornecedorId());
        if (optFornecedor.isEmpty())
            return;

        var telefone = formatFone(optFornecedor.get().getTelefone());
        if (verificarNumeroValido(telefone)) {
            try {
                var message = new StringBuilder();
                compra.getItens().forEach(itemCompra -> {
                    String nomeProduto = produtoService.getNomeProduto(itemCompra.getProdutoId());
                    message.append(String.format("Produto: %s\nQuantidade: %s\nValor: R$ %s\n\n", nomeProduto, itemCompra.getQuantidade(), itemCompra.getValorTotal()));
                });

                enviarWhatsApp("55" + telefone, message.toString());
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Async
    public void enviar(Venda venda) {
        Optional<Cliente> cliente = clienteRepository.findById(venda.getCliente().getIdCliente());
        if (cliente.isEmpty())
            return;

        var telefone = formatFone(cliente.get().getTelefone());
        if (verificarNumeroValido(telefone)) {
            try {
                String encutaredUrl = encurtarUrl(venda.getId());
                var message = String.format("""
                        Comprovante de venda referente a compra em NCP Globo Epi
                        valor total: R$ %s
                        Data: %s
                        Acesse o comprovante Online atraves do link -> %s
                        """, venda.getValorTotal(), venda.getDataVenda(), encutaredUrl);

                enviarWhatsApp("55" + telefone, message);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Async
    public void enviarComprovantePdf(ByteArrayInputStream comprovante, String telefone, String descricao) {
        if (verificarNumeroValido(telefone)) {
            telefone = "55" + formatFone(telefone);
            var url = "https://whatsapp-api-da7eccbe4a89.herokuapp.com/message/doc";
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            try {
                byte[] bytes = FileCopyUtils.copyToByteArray(comprovante);
                var temp = Files.createTempFile("temp", ".pdf").toFile();
                var outputStream = new FileOutputStream(temp);
                outputStream.write(bytes);

                body.add("file", new FileSystemResource(temp));
                body.add("id", telefone);
                body.add("filename", descricao);

                var builder = UriComponentsBuilder.fromUriString(url)
                        .queryParam("key", WHATS_SESSION);

                ResponseEntity<String> responseEntity = restClient.post()
                        .uri(builder.toUriString())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", "Bearer " + WHATS_TOKEN)
                        .body(body)
                        .retrieve()
                        .toEntity(String.class);
                log.info("WhatsApp response status " + responseEntity.getStatusCode());

                temp.deleteOnExit();

            } catch (Exception e) {
                log.info(e.getLocalizedMessage());
                log.error(e.getMessage(), e);
            }
        }
    }

    private String formatFone(String telefone) {
        String result = telefone.trim();
        result = result.replace(" ", "");
        result = result.replace("(", "");
        result = result.replace(")", "");
        result = result.replace("-", "");
        return result;
    }

    private void enviarWhatsApp(String telefone, String message) {
        var url = "https://whatsapp-api-da7eccbe4a89.herokuapp.com/message/text";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("id", telefone);
        body.add("message", message);
        log.info(String.format("Enviando whats para %s", telefone));

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("key", WHATS_SESSION);

        ResponseEntity<String> responseEntity = restClient.post()
                .uri(builder.toUriString())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + WHATS_TOKEN)
                .body(body)
                .retrieve()
                .toEntity(String.class);
        log.info("WhatsApp response status " + responseEntity.getStatusCode());
    }

    private String encurtarUrl(String id) {
        var url = "https://api.encurtador.dev/encurtamentos";
        var urlFinal = "https://api-open-cloud-erp-bc78e9246ecc.herokuapp.com/venda/gerar-comprovante?id=" + id;
        var corpo = "{ \"url\": \"" + urlFinal + "\" }";

        JsonNode responseBody = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(corpo)
                .retrieve()
                .body(JsonNode.class);

        return responseBody.get("urlEncurtada").asString();
    }

    private boolean verificarNumeroValido(String telefone) {
        if (Objects.isNull(telefone))
            return false;

        final var regex = "^[1-9]{2}[0-9]{8,9}$";

        return Pattern.matches(regex, telefone);
    }
}
