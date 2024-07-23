package com.dpacifico.demo_park_api.repository;

import com.dpacifico.demo_park_api.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
