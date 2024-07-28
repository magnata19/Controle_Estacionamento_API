package com.dpacifico.demo_park_api.web.dto.mapper;

import com.dpacifico.demo_park_api.entity.Vaga;
import com.dpacifico.demo_park_api.web.dto.VagaCreateDTO;
import com.dpacifico.demo_park_api.web.dto.VagaResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VagaMapper {

    public static Vaga toVaga(VagaCreateDTO dto) {
        return new ModelMapper().map(dto, Vaga.class);
    }

    public static VagaResponseDTO toDto(Vaga vaga) {
        return new ModelMapper().map(vaga, VagaResponseDTO.class);
    }
}
