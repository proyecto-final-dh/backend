package com.company.model.entity.keys;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPetInterestId implements Serializable {
    private int userId;
    private int petId;
}
