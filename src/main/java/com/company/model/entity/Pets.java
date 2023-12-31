package com.company.model.entity;


import com.company.enums.PetStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Builder
@Table(name="pets")
public class Pets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @NotEmpty(message = "Name is mandatory")
    private String name;

    @Enumerated(EnumType.STRING)
    private PetStatus status;

    private String size;

    private String gender;

    private String description;

    private Integer age;


    @ManyToOne
    @JoinColumn(name = "breed_id")
    private Breeds breed;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserDetails userDetails;


    public Pets(String name, PetStatus status, String size, String gender, String description, Integer age) {
        this.name = name;
        this.status = status;
        this.size = size;
        this.gender = gender;
        this.description = description;
        this.age = age;
    }
}
