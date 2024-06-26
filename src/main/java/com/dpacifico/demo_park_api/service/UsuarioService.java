package com.dpacifico.demo_park_api.service;

import com.dpacifico.demo_park_api.entity.Usuario;
import com.dpacifico.demo_park_api.exception.EntityNotFoundException;
import com.dpacifico.demo_park_api.exception.PasswordInvalidException;
import com.dpacifico.demo_park_api.exception.UsernameUniqueViolation;
import com.dpacifico.demo_park_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        try {
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolation(String.format("Username {%s} já cadastrado. ", usuario.getUsername()));
        }
    }


    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário de id = %s não encontrado!", id))
        );
    }

    //nesse exemplo utilizamos o metodo acima para encontrar um usuario por id
    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
        if (!novaSenha.equals(confirmaSenha)) {
           throw new PasswordInvalidException("As senhas não são iguais, tente novamente.");
        }
        Usuario user = buscarPorId(id);

        if(!user.getPassword().equals(senhaAtual)) {
            throw new PasswordInvalidException("A senha atual não confere!");
        }
        user.setPassword(novaSenha);
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }
}
