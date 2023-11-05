package com.company.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name="pets")
public class Pets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String name1;

    private String status1;

    private String size;

    private String gender;

    private String description1;


    @ManyToOne
    @JoinColumn(name = "breedID")
    private Breeds breed;

    @ManyToOne
    @JoinColumn(name = "ownerID")
    private UserDetails userDetails;


    public Pets(String name1, String status1, String size, String gender, String description1) {
        this.name1 = name1;
        this.status1 = status1;
        this.size = size;
        this.gender = gender;
        this.description1 = description1;
    }
}
