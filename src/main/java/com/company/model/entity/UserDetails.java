package com.company.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "El ID del usuario es obligatorio.")
    @Column(name = "user_id", nullable=false)
    private String userId;

    @NotEmpty(message = "El celular es obligatorio.")
    @Column(name = "cellphone", nullable=false)
    private String cellphone;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @CreationTimestamp
    @JsonIgnore
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date created_at;

    @UpdateTimestamp
    @JsonIgnore
    @Column(name = "updated_at")
    private Date updated_at;

    public UserDetails(String userId, String cellphone, Location location) {
        this.userId = userId;
        this.cellphone = cellphone;
        this.location = location;
    }

    public UserDetails(int id, String userId, String cellphone, Location location) {
        this.id = id;
        this.userId = userId;
        this.cellphone = cellphone;
        this.location = location;
    }
}