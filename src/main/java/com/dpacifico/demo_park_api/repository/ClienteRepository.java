package com.dpacifico.demo_park_api.repository;

import com.dpacifico.demo_park_api.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
