package com.dpacifico.demo_park_api.web.controller;

import com.dpacifico.demo_park_api.entity.Vaga;
import com.dpacifico.demo_park_api.service.VagaService;
import com.dpacifico.demo_park_api.web.dto.VagaCreateDTO;
import com.dpacifico.demo_park_api.web.dto.VagaResponseDTO;
import com.dpacifico.demo_park_api.web.dto.mapper.VagaMapper;
import com.dpacifico.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Vagas", description = "Contém todas as operações relativas ao recurso de uma vaga.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;

    @Operation(summary = "Criar uma nova vaga.", description = "Recurso para criar uma nova vaga." +
    "Requisição exige uso de um bearer token. Acesso restrito a Role = 'ADMIN'",
    security = @SecurityRequirement(name = "security"),
    responses = {
            @ApiResponse(responseCode = "201", description = "Recurso para criar uma nova vaga.",
            headers = @Header(name = HttpHeaders.LOCATION, description = "URL do recurso criado.")),

            @ApiResponse(responseCode = "403", description = "Acesso restrito a Role = 'ADMIN'",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

            @ApiResponse(responseCode = "409", description = "Vaga já cadastrada.",
            content = @Content(mediaType = "application/json;charset=UTF8",
                    schema = @Schema(implementation = ErrorMessage.class))),

            @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos.",
                    content = @Content(mediaType = "application/json;charset=UTF8",
                            schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid VagaCreateDTO dto) { //esse metodo vai retornar o location no insomnia/postman com a uri
        Vaga vaga = VagaMapper.toVaga(dto);
        vagaService.salvar(vaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(vaga.getCodigo())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Localizar uma vaga.", description = "Recuro para retornar uma vaga pelo seu código." +
    "Requisição exige um bearer token. Acesso restrito a Role = 'ADMIN'",
    security = @SecurityRequirement(name = "security"),
    responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json;charset=UTF8",
                    schema = @Schema(implementation = VagaResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Acesso restrito a Role = 'ADMIN'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", content = @Content(mediaType = "application/json;charset=UTF8",
                    schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDTO> getByCodigo(@PathVariable String codigo) {
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));
    }

}
