package com.dpacifico.demo_park_api.web.controller;

import com.dpacifico.demo_park_api.entity.Usuario;
import com.dpacifico.demo_park_api.service.UsuarioService;
import com.dpacifico.demo_park_api.web.dto.UsuarioCreateDto;
import com.dpacifico.demo_park_api.web.dto.UsuarioResponseDto;
import com.dpacifico.demo_park_api.web.dto.UsuarioSenhaDto;
import com.dpacifico.demo_park_api.web.dto.mapper.UsuarioMapper;
import com.dpacifico.demo_park_api.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário.")
@RestController
@RequestMapping("api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Criar novo usuário.", description = "Esse recurso é utilizado para criar novos usuários.",
    //traremos um recurso para resposta do metodo
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),

                    @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

                    @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto usuario) {
        Usuario usuarioSalvo = usuarioService.salvar(UsuarioMapper.toUsuario(usuario));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(usuarioSalvo));
    }

    @Operation(
            summary = "Recuperar um usuário pelo id.", description = "Esse recurso irá recuperar um usuário pelo id.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),

                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado pelo id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") //annotation utilizada para permitir requisicao apenas com perfil ADMIN
    public ResponseEntity<UsuarioResponseDto> getById (@PathVariable Long id) {
        Usuario usuarioId = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toDto(usuarioId));
//        return ResponseEntity.status(HttpStatus.OK).body(usuarioId);
    }

    @Operation(
            summary = "Atualizar senha", description = "Recurso para atualizar a senha de um usuário.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),

                    @ApiResponse(responseCode = "400", description = "As senhas não conferem.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado para alterar a senha.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

                    @ApiResponse(responseCode = "422", description = "Campos inválidos ou mal formatados.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto) {
        Usuario usuarioAlterado = usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar todos usuários.", description = "Recurso para listar todos usuários cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso!",
                    content = @Content(mediaType = "application/json",
//                    schema = @Schema(implementation = UsuarioResponseDto.class) essa forma também retornou um array
                    array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class))))
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDto>> getAll() {
        List<Usuario> listaDeUsuarios = usuarioService.getAll();
        return ResponseEntity.ok(UsuarioMapper.toListDto(listaDeUsuarios));
    }
}
