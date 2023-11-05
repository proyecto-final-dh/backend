package com.company.model.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pets")

public class Pets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "El nombre de la mascota es obligatorio.")
    private String name;


    private String status;

    private String size;

    private String gender;

    private String description;


    @ManyToOne
    @JoinColumn(name = "breed_id")
    private Breeds breed;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserDetails userDetails;


    public Pets(String name, String status, String size, String gender, String description) {
        this.name = name;
        this.status = status;
        this.size = size;
        this.gender = gender;
        this.description = description;
    }

}
