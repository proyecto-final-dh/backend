package com.company.model.dto;

import com.company.enums.PetStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportByStatusDto {
    private PetStatus status;
    private List<AdoptionsByDateDto> result;
}
