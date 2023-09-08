package com.open.erp.openerp.dominio.venda.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.open.erp.openerp.dominio.cliente.model.Cliente;
import com.open.erp.openerp.dominio.cliente.repository.ClienteRepository;
import com.open.erp.openerp.dominio.venda.model.Venda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@EnableAsync
public class EnviarComprovanteVendaWhatsAppService {
    Logger log = LoggerFactory.getLogger(EnviarComprovanteVendaWhatsAppService.class);

    @Autowired
    private ClienteRepository clienteRepository;
    @Value("${whats-api.token}")
    private String WHATS_TOKEN;
    @Value("${whats-api.session}")
    private String WHATS_SESSION;

    @Async
    public void enviar(Venda venda) {
        Optional<Cliente> cliente = clienteRepository.findById(venda.getCliente().getIdCliente());
        if (cliente.isEmpty())
            return;

        var telefone = formatFone(cliente.get().getTelefone());
        if (verificarNumeroValido(telefone)) {
            try {
                String encutaredUrl = encurtarUrl(venda.getId());
                enviarWhatsApp(encutaredUrl, "55" + telefone, venda);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
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

    private void enviarWhatsApp(String encutaredUrl, String telefone, Venda venda) {
        var url = "https://whatsapp-api-da7eccbe4a89.herokuapp.com/message/text";
        var authorization = "Bearer " + WHATS_TOKEN;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", authorization);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        var message = String.format("""
                Comprovante de venda referente a compra em NCP Globo Epi
                valor total: R$ %s
                Data: %s
                Acesse o comprovante Online atraves do link -> %s
                """, venda.getValorTotal(), venda.getDataVenda(), encutaredUrl);
        body.add("id", telefone);
        body.add("message", message);
        log.info(String.format("Enviando whats para %s", telefone));
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("key", WHATS_SESSION);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, requestEntity, String.class);
        log.info("WhatsApp response status " + responseEntity.getStatusCode());
    }

    private String encurtarUrl(String id) {
        var url = "https://api.encurtador.dev/encurtamentos";
        var urlFinal = "https://api-open-cloud-erp-bc78e9246ecc.herokuapp.com/venda/gerar-comprovante?id=" + id;
        var corpo = "{ \"url\": \"" + urlFinal + "\" }";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(corpo, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, JsonNode.class);

        return responseEntity.getBody().get("urlEncurtada").asText();
    }

    private boolean verificarNumeroValido(String telefone) {
        if (Objects.isNull(telefone))
            return false;

        final var regex = "^[1-9]{2}[0-9]{8,9}$";

        return Pattern.matches(regex, telefone);
    }
}
