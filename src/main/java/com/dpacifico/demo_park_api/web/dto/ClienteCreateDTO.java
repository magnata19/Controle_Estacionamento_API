package com.dpacifico.demo_park_api.web.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClienteCreateDTO {

    @NotBlank
    @Size(min = 5, max = 100)
    private String nome;

    @NotBlank
    @CPF
    @Size(min = 11, max = 11)
    private String cpf;
}
