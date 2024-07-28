package com.dpacifico.demo_park_api.service;

import com.dpacifico.demo_park_api.entity.Cliente;
import com.dpacifico.demo_park_api.exception.CpfUniqueViolation;
import com.dpacifico.demo_park_api.exception.EntityNotFoundException;
import com.dpacifico.demo_park_api.repository.ClienteRepository;
import com.dpacifico.demo_park_api.repository.projection.ClienteProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        try {
            Cliente save = clienteRepository.save(cliente);
            return save;
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolation(String.format("CPF '%s' já está cadastrado.", cliente.getCpf()));
        }
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuário de id = %s não encontrado.", id))
        );
    }

    @Transactional(readOnly = true)
    public Page<ClienteProjection> buscarTodos(Pageable pageable){
        return clienteRepository.findAllPageable(pageable);
    }

    public Cliente buscarUsuarioPorId(Long id) {
        return clienteRepository.findByUsuarioId(id);
    }
}
