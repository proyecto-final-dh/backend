package com.company.model.dto;

import com.company.enums.PetStatus;
import com.company.model.entity.Breeds;
import com.company.model.entity.UserDetails;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompletePetDto {
    private int id;
    private String name;
    private PetStatus status;
    private String size;
    private String gender;
    private String description;
    private Integer age;
    private Breeds breed;
    private UserDetails userDetails;
    private List<ImageWithTitle> images;
}
