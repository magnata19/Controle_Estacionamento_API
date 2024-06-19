package com.dpacifico.demo_park_api.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UsuarioCreateDto {

    private String username;
    private String password;
}
