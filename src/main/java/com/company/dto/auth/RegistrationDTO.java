package com.company.dto.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class RegistrationDTO {
    private int id;
    @NotEmpty(message = "Name is empty or null")
    @NotBlank(message = "Name has empty space")
    private String name;
    @NotEmpty(message = "Surname is empty or null")
    @NotBlank(message = "Surname has empty space")
    @Size(min = 3, max = 15, message = "Surname length must be has 3 - 15 letters")
    private String surname;
    private String login;
    private String password;
    @Email(message = "email no correct")
    private String email;
}
