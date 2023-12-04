package com.company.model.entity;


import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserKeycloak {

    private String idUsuario;
    private String userName;
    private String name;
    private String lastname;
    private String email;



}
