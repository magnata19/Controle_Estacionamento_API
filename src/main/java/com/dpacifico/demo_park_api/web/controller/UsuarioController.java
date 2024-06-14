package com.dpacifico.demo_park_api.web.controller;

import com.dpacifico.demo_park_api.entity.Usuario;
import com.dpacifico.demo_park_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {
        Usuario usuarioSalvo = usuarioService.salvar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById (@PathVariable Long id) {
        Usuario usuarioId = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuarioId);
//        return ResponseEntity.status(HttpStatus.OK).body(usuarioId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Usuario> updatePassword( @PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario usuarioAlterado = usuarioService.editarSenha(id, usuario.getPassword());
        return ResponseEntity.ok(usuarioAlterado);
    }
}
