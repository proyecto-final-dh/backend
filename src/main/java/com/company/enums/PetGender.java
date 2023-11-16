package com.company.enums;

import java.util.Arrays;

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
}
