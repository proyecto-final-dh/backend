package com.company.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
public class Species {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String name1;
    public Species() {
    }
    public Species(String name1) {
        this.name1 = name1;
    }
    public Species(int id, String name1) {
        this.id = id;
        this.name1 = name1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name1;
    }

    public void setName(String name) {
        this.name1 = name;
    }
}