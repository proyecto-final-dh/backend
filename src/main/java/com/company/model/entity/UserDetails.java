package com.company.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
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
    @NotEmpty(message = "El ID del usuario es obligatorio.")
    @Column(name = "id", nullable=false)
    private String id;

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

    public UserDetails(String id, String cellphone, Location location) {
        this.id = id;
        this.cellphone = cellphone;
        this.location = location;
    }

}