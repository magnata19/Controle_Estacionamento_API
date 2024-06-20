package com.dpacifico.demo_park_api.web.controller;

import com.dpacifico.demo_park_api.entity.Usuario;
import com.dpacifico.demo_park_api.service.UsuarioService;
import com.dpacifico.demo_park_api.web.dto.UsuarioCreateDto;
import com.dpacifico.demo_park_api.web.dto.UsuarioResponseDto;
import com.dpacifico.demo_park_api.web.dto.UsuarioSenhaDto;
import com.dpacifico.demo_park_api.web.dto.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@RequestBody UsuarioCreateDto usuario) {
        Usuario usuarioSalvo = usuarioService.salvar(UsuarioMapper.toUsuario(usuario));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(usuarioSalvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById (@PathVariable Long id) {
        Usuario usuarioId = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toDto(usuarioId));
//        return ResponseEntity.status(HttpStatus.OK).body(usuarioId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UsuarioSenhaDto dto) {
        Usuario usuarioAlterado = usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        List<Usuario> listaDeUsuarios = usuarioService.getAll();
        return ResponseEntity.ok(listaDeUsuarios);
    }
}
