package com.example.demo.controller.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String fullName;

    private String birthDate;

    private Double weight;

    private Double height;
    private String email;    
    private String password;    
    private Long roleId; 

}
