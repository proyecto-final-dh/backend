package com.company.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class SaveLocationDTO {

    @NotEmpty(message = "Country is mandatory")
    private String country;
    @NotEmpty(message = "City is mandatory")
    private String city;
    @NotEmpty(message = "State is mandatory")
    private String state;
}
