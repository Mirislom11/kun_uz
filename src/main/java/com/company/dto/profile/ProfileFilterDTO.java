package com.company.dto.profile;

import com.company.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileFilterDTO {
    private String name;
    private String surname;
    private String login;
    private String  password;
    private String email;
    private ProfileRole profileRole;
}
