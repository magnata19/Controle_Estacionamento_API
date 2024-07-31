package com.dpacifico.demo_park_api.repository;

import com.dpacifico.demo_park_api.entity.ClienteVaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteVagaRepository extends JpaRepository<ClienteVaga, Long> {
}