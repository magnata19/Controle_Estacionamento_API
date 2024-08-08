package com.dpacifico.demo_park_api.service;

import com.dpacifico.demo_park_api.entity.Cliente;
import com.dpacifico.demo_park_api.entity.ClienteVaga;
import com.dpacifico.demo_park_api.entity.Vaga;
import com.dpacifico.demo_park_api.util.EstacionamentoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EstacionamentoService {

   private final ClienteVagaService clienteVagaService;
   private final ClienteService clienteService;
   private final VagaService vagaService;

   public ClienteVaga checkIn(ClienteVaga clienteVaga) {
      Cliente cliente = clienteService.buscarPorCpf(clienteVaga.getCliente().getCpf());
      clienteVaga.setCliente(cliente);
      Vaga vaga = vagaService.buscarPorVagaLivre(); //achou vaga livre
      vaga.setStatus(Vaga.StatusVaga.OCUPADA); //vaga passa a ser ocupada
      clienteVaga.setVaga(vaga);
      clienteVaga.setDataEntrada(LocalDateTime.now());
      clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibo());
      return clienteVagaService.salvar(clienteVaga);
   }

   @Transactional
   public ClienteVaga checkOut(String recibo) {
      ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo);
      LocalDateTime dataSaida = LocalDateTime.now();

      BigDecimal valor = EstacionamentoUtils.calcularCusto(clienteVaga.getDataEntrada(), dataSaida);
      clienteVaga.setValor(valor);

      long totalDeVezes = clienteVagaService.getTotalDeVezesEstacionamentoCompleto(clienteVaga.getCliente().getCpf());
      BigDecimal desconto = EstacionamentoUtils.calcularDesconto(valor, totalDeVezes);
      clienteVaga.setDesconto(desconto);
      clienteVaga.setDataSaida(dataSaida);
      clienteVaga.getVaga().setStatus(Vaga.StatusVaga.LIVRE);
      return clienteVagaService.salvar(clienteVaga);
   }
}
