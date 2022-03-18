package com.company.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationDTO {
    @NotEmpty(message = "login not empty or null")
    @NotBlank(message = "login must not be blank")
    private String login;
    @Size(min = 8, max = 16, message = "password length must be min 8 max 16")
    private String password;

}
