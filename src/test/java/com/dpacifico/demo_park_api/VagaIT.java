package com.dpacifico.demo_park_api;

import com.dpacifico.demo_park_api.web.dto.VagaCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vagas/vagas-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vagas/vagas-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VagaIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void criarVaga_ComDadosValidos_RetornarLocationStatus201() {
        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient, "davids@david2.com","123321"))
                .bodyValue(new VagaCreateDTO("A-05","LIVRE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void criarVaga_ComCodigoJaExistente_RetornarErrorMessageComStatus409() {
        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient, "davids@david2.com","123321"))
                .bodyValue(new VagaCreateDTO("A-01","LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("status").isEqualTo(409)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
    }

    @Test
    public void criarVaga_ComDadosInvalidos_RetornarErrorMessageComStatus422() {
        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"davids@david2.com","123321"))
                .bodyValue(new VagaCreateDTO("A-400","DESOCUPADA"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
    }

    @Test
    public void buscarVaga_ComCodigoExistente_RetornarVagaComStatus200() {
        testClient
                .get()
                .uri("/api/v1/vagas/{codigo}","A-01")
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"davids@david2.com","123321"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(10)
                .jsonPath("codigo").isEqualTo("A-01")
                .jsonPath("status").isEqualTo("LIVRE");
    }

    @Test
    public void buscarVaga_ComCodigoInexistente_RetornarErrorMessageComStatus404(){
        testClient
                .get()
                .uri("/api/v1/vagas/{codigo}","A-500")
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"davids@david2.com","123321"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vagas/A-500");
    }

    @Test
    public void buscarVaga_ComCodigoExistenteComPerfilSemAutorizacao_RetornarErrorMessageComStatus403() {
        testClient
                .get()
                .uri("/api/v1/vagas/{codigo}","A-01")
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"pacific@david2.com","123321"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vagas/A-01");
    }

    @Test
    public void criarVaga_ComDadosValidosComPerfilSemAutorizacao_RetornarErrorMessageComStatus403(){
        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"pacific@david2.com","123321"))
                .bodyValue(new VagaCreateDTO("A-55","LIVRE"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");
    }

}
