package com.dpacifico.demo_park_api.web.controller;

import com.dpacifico.demo_park_api.entity.Vaga;
import com.dpacifico.demo_park_api.service.VagaService;
import com.dpacifico.demo_park_api.web.dto.VagaCreateDTO;
import com.dpacifico.demo_park_api.web.dto.VagaResponseDTO;
import com.dpacifico.demo_park_api.web.dto.mapper.VagaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;

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

    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDTO> getByCodigo(@PathVariable String codigo) {
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));
    }

}
