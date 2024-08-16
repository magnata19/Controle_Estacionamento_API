package com.dpacifico.demo_park_api;

import com.dpacifico.demo_park_api.web.dto.EstacionamenteCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class EstacionamentoIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void criarCheckin_ComDadosValidos_RetornarCreatedAndLocation() {
        EstacionamenteCreateDTO createDTO = EstacionamenteCreateDTO.builder()
                .placa("JKL-0392").marca("MAZDA").modelo("RX7")
                .cor("PRETO").clienteCpf("19796804859")
                .build();

        testClient.post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"davids@david2.com","123321"))
                .bodyValue(createDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("placa").isEqualTo("JKL-0392")
                .jsonPath("marca").isEqualTo("MAZDA")
                .jsonPath("modelo").isEqualTo("RX7")
                .jsonPath("cor").isEqualTo("PRETO")
                .jsonPath("clienteCpf").isEqualTo("19796804859")
                .jsonPath("recibo").exists()
                .jsonPath("dataEntrada").exists()
                .jsonPath("vagaCodigo").exists();

    }

    @Test
    public void criarCheckin_ComRoleCliente_RetornarErrorMessageComStatus403() {
        EstacionamenteCreateDTO createDTO = EstacionamenteCreateDTO.builder()
                .placa("JKL-0392").marca("MAZDA").modelo("RX7")
                .cor("PRETO").clienteCpf("19796804859")
                .build();

        testClient.post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"pacific@david2.com","123321"))
                .bodyValue(createDTO)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in");
    }

    @Test
    public void criarCheckin_ComDadosInvalidos_RetornarErrorMessageComStatus422() {
        EstacionamenteCreateDTO createDTO = EstacionamenteCreateDTO.builder()
                .placa("").marca("").modelo("")
                .cor("").clienteCpf("")
                .build();

        testClient.post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"davids@david2.com","123321"))
                .bodyValue(createDTO)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in");
    }

    @Test
    public void criarCheckin_ComCpfInexistente_RetornarErrorMessageComStatus404() {
        EstacionamenteCreateDTO createDTO = EstacionamenteCreateDTO.builder()
                .placa("JKL-0392").marca("MAZDA").modelo("RX7")
                .cor("PRETO").clienteCpf("00756168074")
                .build();

        testClient.post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"davids@david2.com","123321"))
                .bodyValue(createDTO)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in");
    }

    @Sql(scripts = "/sql/estacionamentos/estacionamentos-insert-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/estacionamentos/estacionamentos-delete-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void criarCheckin_ComVagasOcupadas_RetornarErrorMessageComStatus404() {
        EstacionamenteCreateDTO createDTO = EstacionamenteCreateDTO.builder()
                .placa("JKL-0392").marca("MAZDA").modelo("RX7")
                .cor("PRETO").clienteCpf("19796804859")
                .build();

        testClient.post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"davids@david2.com","123321"))
                .bodyValue(createDTO)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in");
    }

    @Test
    public void buscarCheckin_ComPerfilAdmin_RetornarDadosComStatus200() {
        testClient
                .get()
                .uri("/api/v1/estacionamentos/check-in/{recibo}","20240803-212400")
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"davids@david2.com","123321"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("JDM-3432")
                .jsonPath("marca").isEqualTo("MAZDA")
                .jsonPath("modelo").isEqualTo("MAZDA RX7")
                .jsonPath("cor").isEqualTo("PRETO")
                .jsonPath("clienteCpf").isEqualTo("19796804859")
                .jsonPath("recibo").isEqualTo("20240803-212400")
                .jsonPath("dataEntrada").isEqualTo("2024-03-15 10:23:43")
                .jsonPath("vagaCodigo").isEqualTo("A-01");

    }

    @Test
    public void buscarCheckin_ComPerfilCliente_RetornarDadosComStatus200() {
        testClient
                .get()
                .uri("/api/v1/estacionamentos/check-in/{recibo}","20240803-212400")
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"pacific@david2.com","123321"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("JDM-3432")
                .jsonPath("marca").isEqualTo("MAZDA")
                .jsonPath("modelo").isEqualTo("MAZDA RX7")
                .jsonPath("cor").isEqualTo("PRETO")
                .jsonPath("clienteCpf").isEqualTo("19796804859")
                .jsonPath("recibo").isEqualTo("20240803-212400")
                .jsonPath("dataEntrada").isEqualTo("2024-03-15 10:23:43")
                .jsonPath("vagaCodigo").isEqualTo("A-01");
    }

    @Test
    public void buscarCheckin_ComReciboInexistente_RetornarDadosComStatus404() {
        testClient
                .get()
                .uri("/api/v1/estacionamentos/check-in/{recibo}", "20240803-999999")
                .headers(JwtAuthentication.getHeaderAuthentication(testClient, "pacific@david2.com", "123321"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in/20240803-999999");
    }

    @Sql(scripts = "/sql/estacionamentos/estacionamentos-insert-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/estacionamentos/estacionamentos-delete-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void criarCheckout_ComReciboExistente_RetornarSucessoStatus200() {
        testClient
                .put()
                .uri("/api/v1/estacionamentos/check-out/{recibo}", "20240803-212400")
                .headers(JwtAuthentication.getHeaderAuthentication(testClient, "davids@david2.com", "123321"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("JDM-3432")
                .jsonPath("marca").isEqualTo("MAZDA")
                .jsonPath("modelo").isEqualTo("MAZDA RX7")
                .jsonPath("cor").isEqualTo("PRETO")
                .jsonPath("dataEntrada").isEqualTo("2024-03-15 10:23:43")
                .jsonPath("clienteCpf").isEqualTo("19796804859")
                .jsonPath("vagaCodigo").isEqualTo("A-01")
                .jsonPath("recibo").isEqualTo("20240803-212400")
                .jsonPath("dataSaida").exists()
                .jsonPath("valor").exists()
                .jsonPath("desconto").exists();
    }

    @Sql(scripts = "/sql/estacionamentos/estacionamentos-insert-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/estacionamentos/estacionamentos-delete-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void criarCheckout_ComReciboInexistente_RetornarSucessoStatus404() {
        testClient
                .put()
                .uri("/api/v1/estacionamentos/check-out/{recibo}", "20240803-000000")
                .headers(JwtAuthentication.getHeaderAuthentication(testClient, "davids@david2.com", "123321"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("method").isEqualTo("PUT")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-out/20240803-000000");
    }

    @Sql(scripts = "/sql/estacionamentos/estacionamentos-insert-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/estacionamentos/estacionamentos-delete-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void criarCheckout_ComPerfilCliente_RetornarSucessoStatus403() {
        testClient
                .put()
                .uri("/api/v1/estacionamentos/check-out/{recibo}", "20240803-212400")
                .headers(JwtAuthentication.getHeaderAuthentication(testClient, "pacific@david2.com", "123321"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("method").isEqualTo("PUT")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-out/20240803-212400");
    }


}
