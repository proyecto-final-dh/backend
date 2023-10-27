package com.company.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name="locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;
    @NotEmpty(message = "Country is mandatory")
    private String country;
    @NotEmpty(message = "City is mandatory")
    private String city;
    @NotEmpty(message = "State is mandatory")
    private String state;

    public Location(String country, String city, String state) {
        this.country = country;
        this.city = city;
        this.state = state;
    }
}
