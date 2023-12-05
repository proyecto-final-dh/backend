package com.company.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralReportsDto {

    private int enAdopcionCount;
    private int adoptadasCount;
    private int conQrCount;
    private Double averageTime;
}
