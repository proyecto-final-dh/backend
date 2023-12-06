package com.company.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PetStatus {
    MASCOTA_PROPIA("MASCOTA_PROPIA", "Mascota propia"),
    EN_ADOPCION("EN_ADOPCION", "En adopción"),
    ADOPTADA("ADOPTADA", "Adoptada");

    PetStatus(String id, String name){
        this.id = id;
        this.name = name;
    }

    private String id;
    private String name;

    public static boolean isValidStatus(String status) {
        return Arrays.stream(PetStatus.values()).anyMatch(e -> e.getId().equalsIgnoreCase(status));
    }


    public static PetStatus getStatusById(String status) {
        return Arrays.stream(PetStatus.values()).filter(e -> e.getId().equalsIgnoreCase(status)).findFirst().get();
    }
}
