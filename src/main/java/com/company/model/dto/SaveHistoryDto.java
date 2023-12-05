package com.company.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveHistoryDto {
    @NotNull(message = "La fecha es obligatoria.")
    private Date date;

    @NotNull(message = "El ID de la mascota es obligatorio.")
    private int petId;

    @NotNull(message = "El ID de la user es obligatorio.")
    private int userDetailsId;

    private String status;

}
