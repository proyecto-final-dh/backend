package com.company.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserDetailsDto {
    @NotEmpty(message = "El ID del usuario es obligatorio.")
    private String userId;

    @NotEmpty(message = "El celular es obligatorio.")
    private String cellphone;

    @NotNull(message = "El ID del a ubicación es obligatorio.")
    private int locationId;
}
