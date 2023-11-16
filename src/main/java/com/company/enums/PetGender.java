package com.company.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PetGender {
    MACHO("MACHO"),
    HEMBRA("HEMBRA");

    PetGender(String id) {
        this.id = id;
    }

    private String id;

    public static boolean isValidGender(String gender) {
        return Arrays.stream(PetStatus.values()).anyMatch(e -> e.getId().equalsIgnoreCase(gender));
    }

    public static PetGender getGenderById(String gender) {
        return Arrays.stream(PetGender.values()).filter(e -> e.getId().equalsIgnoreCase(gender)).findFirst().get();
    }
}
