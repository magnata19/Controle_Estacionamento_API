package com.dpacifico.demo_park_api.web.controller;

import com.dpacifico.demo_park_api.entity.ClienteVaga;
import com.dpacifico.demo_park_api.service.ClienteVagaService;
import com.dpacifico.demo_park_api.service.EstacionamentoService;
import com.dpacifico.demo_park_api.web.dto.EstacionamenteCreateDTO;
import com.dpacifico.demo_park_api.web.dto.EstacionamentoResponseDTO;
import com.dpacifico.demo_park_api.web.dto.mapper.ClienteVagaMapper;
import com.dpacifico.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Estacionamentos", description = "Operações de registro de entrada e saída de um veículo do estacionamento.")
@RestController
@RequestMapping("/api/v1/estacionamentos")
@RequiredArgsConstructor
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;
    private final ClienteVagaService clienteVagaService;

    @Operation(summary = "Operação de check-in", description = "Recurso para dar entrada de um veículo no estacionamento." +
        "Requisição exige o uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso!",
            content = @Content(mediaType = "application/json;charset=UTF8", schema = @Schema(implementation = EstacionamentoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Causas possíveis: </br>" +
                    " - CPF do cliente não cadastrado no sistema; </br>" +
                    " - Nenhuma vaga livre foi localizada;",
            content = @Content(mediaType = "application/json;charset=UTF8", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos.",
            content = @Content(mediaType = "application/json;charset=UTF8", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE.",
            content = @Content(mediaType = "application/json;charset=UTF8", schema = @Schema(implementation = ErrorMessage.class))),

            }
    )
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDTO> checkIn(@RequestBody @Valid EstacionamenteCreateDTO dto) {
        ClienteVaga clienteVaga = ClienteVagaMapper.toVaga(dto);
        estacionamentoService.checkIn(clienteVaga);
        EstacionamentoResponseDTO responseDTO = ClienteVagaMapper.toDto(clienteVaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();

        return ResponseEntity.created(location).body(responseDTO);
    }

    @Operation(summary = "Localizar um veículo estacionado.", description = "Recurso para retornar um veículo estacionado " +
    "pelo nº do recibo. Requisição exige o uso de um bearer token",
    security = @SecurityRequirement(name = "security"),
            parameters = {
            @Parameter(in = ParameterIn.PATH, name = "recibo", description = "Número do recibo gerado pelo check-in")
            },
    responses = {
            @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso!",
            content = @Content(mediaType = "application/json;charset=UTF8", schema = @Schema(implementation = EstacionamentoResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Número de recibo não encontrado.",
                    content = @Content(mediaType = "application/json;charset=UTF8", schema = @Schema(implementation = ErrorMessage.class)))
    }
    )
    @GetMapping("/check-in/{recibo}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<EstacionamentoResponseDTO> getByRecibo(@PathVariable String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo);
        EstacionamentoResponseDTO dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/checkout/{recibo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDTO> checkOut(@PathVariable String recibo) {
        ClienteVaga clienteVaga = estacionamentoService.checkOut(recibo);
        EstacionamentoResponseDTO dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }
}
