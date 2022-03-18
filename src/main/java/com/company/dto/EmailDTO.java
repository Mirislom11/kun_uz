package com.company.dto;

import com.company.enums.EmailStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {
    private int id;
    @Email
    private String fromAccount;
    @Email
    private String toAccount;
    private String title;
    private String content;
    private LocalDateTime createdDate;
    private EmailStatus emailStatus;
}
