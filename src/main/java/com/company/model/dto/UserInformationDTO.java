package com.company.model.dto;

import com.company.model.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInformationDTO {
    private String userId;
    private Integer userDetailsId;
    private String name;
    private String lastname;
    private String email;
    private String cellphone;
    private Location location;
}
