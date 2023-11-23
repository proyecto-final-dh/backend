package com.company.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PetSize {
    PEQUEÑO("PEQUEÑO"),
    MEDIANO("MEDIANO"),
    GRANDE("GRANDE");

    PetSize(String id) {
        this.id = id;
    }

    private String id;

    public static boolean isValidSize(String size) {
        return Arrays.stream(PetSize.values()).anyMatch(e -> e.getId().equalsIgnoreCase(size));
    }

    public static PetSize getSizeById(String size) {
        return Arrays.stream(PetSize.values()).filter(e -> e.getId().equalsIgnoreCase(size)).findFirst().get();
    }
}
