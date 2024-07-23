package com.dpacifico.demo_park_api.web.dto.mapper;

import com.dpacifico.demo_park_api.entity.Cliente;
import com.dpacifico.demo_park_api.web.dto.ClienteCreateDTO;
import com.dpacifico.demo_park_api.web.dto.ClienteResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDTO clienteCreateDTO) {
        return new ModelMapper().map(clienteCreateDTO, Cliente.class); // nesse caso, dto é de onde vem e o Cliente.class é pra onde vai
    }

    public static ClienteResponseDTO toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDTO.class);
    }
}
