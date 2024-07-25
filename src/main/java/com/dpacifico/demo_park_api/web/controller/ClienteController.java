package com.dpacifico.demo_park_api.web.controller;

import com.dpacifico.demo_park_api.entity.Cliente;
import com.dpacifico.demo_park_api.jwt.JwtUserDetails;
import com.dpacifico.demo_park_api.service.ClienteService;
import com.dpacifico.demo_park_api.service.UsuarioService;
import com.dpacifico.demo_park_api.web.dto.ClienteCreateDTO;
import com.dpacifico.demo_park_api.web.dto.ClienteResponseDTO;
import com.dpacifico.demo_park_api.web.dto.mapper.ClienteMapper;
import com.dpacifico.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clientes", description = "Contém todas as operações relativas aos recursos de um cliente.")
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo cliente.",
            description = "Recurso para criar um novo cliente vinculado a um usuário cadastrado." +
            " Requisição exigo o uso ",
    responses = {
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso!",
            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Cliente CPF já possui cadastro no sistema",
                    content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos.",
                    content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil ADMIN.",
                    content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
    }
    )
    @PostMapping("/salvar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> create(@Valid @RequestBody ClienteCreateDTO dto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "Localizar um cliente", description = "Recurso para localizar um cliente pelo ID."+
            "Requisição exige um bearer token. Acesso restrito a Role = 'ADMIN'.",
    responses = {
            @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso com sucesso!",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> getById(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.status(200).body(ClienteMapper.toDto(cliente));
    }
}
