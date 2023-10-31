package com.company.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.antlr.v4.runtime.misc.NotNull;


@Entity
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


    public Pets(String name1, String status1, String size, String gender, String description1) {
        this.name1 = name1;
        this.status1 = status1;
        this.size = size;
        this.gender = gender;
        this.description1 = description1;
    }

    public Pets(int id, String name1, String status1, String size, String gender, String description1) {
        this.id = id;
        this.name1 = name1;
        this.status1 = status1;
        this.size = size;
        this.gender = gender;
        this.description1 = description1;
    }


    public Pets() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }


    @Override
    public String toString() {
        return "Pets{" +
                "id=" + id +
                ", name1='" + name1 + '\'' +
                ", status1='" + status1 + '\'' +
                ", size='" + size + '\'' +
                ", gender='" + gender + '\'' +
                ", description1='" + description1 + '\'' +
                '}';
    }
}
