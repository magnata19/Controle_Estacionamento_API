package com.dpacifico.demo_park_api.web.controller;

import com.dpacifico.demo_park_api.entity.Cliente;
import com.dpacifico.demo_park_api.jwt.JwtUserDetails;
import com.dpacifico.demo_park_api.service.ClienteService;
import com.dpacifico.demo_park_api.service.UsuarioService;
import com.dpacifico.demo_park_api.web.dto.ClienteCreateDTO;
import com.dpacifico.demo_park_api.web.dto.ClienteResponseDTO;
import com.dpacifico.demo_park_api.web.dto.mapper.ClienteMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> create(@Valid @RequestBody ClienteCreateDTO dto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }
}
