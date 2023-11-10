package com.company.model.dto;

import com.company.enums.PetStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetWithImagesDto {
    private int id;
    private String name;
    private PetStatus status;
    private String size;
    private String gender;
    private String description;
    private Integer breed_id;
    private Integer owner_id;
    private List<ImageWithTitle> images;
}
