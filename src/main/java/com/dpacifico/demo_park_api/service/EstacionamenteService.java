package com.dpacifico.demo_park_api.service;

import com.dpacifico.demo_park_api.repository.ClienteVagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstacionamenteService {

   private final ClienteVagaService clienteVagaService;
   private final ClienteService clienteService;
   private final VagaService vagaServicea;
}
