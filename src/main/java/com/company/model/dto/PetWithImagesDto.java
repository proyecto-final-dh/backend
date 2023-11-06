package com.company.model.dto;

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
    private String status;
    private String size;
    private String gender;
    private String description;
    private List<ImageWithTitle> images;
}
