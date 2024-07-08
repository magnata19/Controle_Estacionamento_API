package com.dpacifico.demo_park_api.web.controller;

import com.dpacifico.demo_park_api.jwt.JwtToken;
import com.dpacifico.demo_park_api.jwt.JwtUserDetailsService;
import com.dpacifico.demo_park_api.web.dto.UsuarioLoginDto;
import com.dpacifico.demo_park_api.web.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.AuthenticationException;

@Slf4j
@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class AutenticacaoController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@Valid @RequestBody UsuarioLoginDto dto,
                                        HttpServletRequest request) {
        log.info("Processo de autenticacao pelo login {}", dto.getUsername());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            authenticationManager.authenticate(authenticationToken);

            JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            log.warn("Bad credentials from username {}", dto.getUsername());
        }
        return ResponseEntity.badRequest().body
                (new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais inválidas."));
    }
}
