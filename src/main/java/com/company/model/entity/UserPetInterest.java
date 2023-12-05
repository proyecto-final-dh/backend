package com.company.model.entity;

import com.company.model.entity.keys.UserPetInterestId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_pet_interest")
@IdClass(UserPetInterestId.class)
public class UserPetInterest {
    @Id
    @Column(name = "user_id")
    private int userId;
    @Id
    @Column(name = "pet_id")
    private int petId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserDetails user;

    @ManyToOne
    @JoinColumn(name = "pet_id", insertable = false, updatable = false)
    private Pets pet;
}
