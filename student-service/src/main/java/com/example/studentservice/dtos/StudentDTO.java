package com.example.studentservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private int id;
    private String  first_name;
    private String  last_name;
    private String  email;
    private AddressDTO address;
}
