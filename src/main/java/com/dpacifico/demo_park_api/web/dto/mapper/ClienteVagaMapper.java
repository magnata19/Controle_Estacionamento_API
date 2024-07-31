package com.dpacifico.demo_park_api.web.dto.mapper;

import com.dpacifico.demo_park_api.entity.ClienteVaga;
import com.dpacifico.demo_park_api.web.dto.EstacionamenteCreateDTO;
import com.dpacifico.demo_park_api.web.dto.EstacionamentoResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    public static ClienteVaga toVaga(EstacionamenteCreateDTO dto) {
        return new ModelMapper().map(dto, ClienteVaga.class);
    }

    public static EstacionamentoResponseDTO toDto(ClienteVaga vaga) {
        return new ModelMapper().map(vaga, EstacionamentoResponseDTO.class);
    }
}
