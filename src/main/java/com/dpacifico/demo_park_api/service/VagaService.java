package com.dpacifico.demo_park_api.service;

import com.dpacifico.demo_park_api.entity.Vaga;
import com.dpacifico.demo_park_api.exception.CodigoUniqueViolationException;
import com.dpacifico.demo_park_api.exception.EntityNotFoundException;
import com.dpacifico.demo_park_api.repository.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VagaService {

    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga salvar(Vaga vaga) {
        try {
            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException ex) {
            throw new CodigoUniqueViolationException(String.format("Vaga com código {%s} já cadastrado,", vaga.getCodigo()));
        }
    }

    public Vaga buscarPorCodigo(String codigo) {
        return vagaRepository.findByCodigo(codigo).orElseThrow(
                () -> new EntityNotFoundException(String.format("Vaga com código {%s} não encontrada", codigo))
        );
    }
}
