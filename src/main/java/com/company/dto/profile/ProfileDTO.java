package com.company.dto.profile;

import com.company.enums.ProfileRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;
    @NotEmpty(message = "name empty or null")
    @NotBlank(message = "name has empty space")
    @Size(min = 3, max = 15, message = "name length must be 3 max max 15")
    private String name;
    @NotEmpty(message = "surname empty or null")
    @Size(min = 3, max = 15, message = "surname length must be 3 max max 15")
    @NotBlank(message = "surname has empty space")
    private String surname;
    @NotEmpty(message = "login empty or null")
    private String login;
    private String password;
    @Email
    private String email;
    private ProfileRole profileRole;
    private String jwt; // token
}
