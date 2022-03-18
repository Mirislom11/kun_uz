package com.company.entity;

import com.company.enums.EmailStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

// id, from_account,to_account,created_date,status(used, not used),used_date

@Getter
@Setter
@Entity
@Table(name = "email")
public class EmailEntity extends BasedEntity{

    @Column(name = "from_account")
    private String fromAccount;
    @Column(name = "to_account")
    private String toAccount;
    @Column(name = "content")
    private String content;
    @Column(name = "subject")
    private String title;

    @Column(name = "email_status")
    private EmailStatus emailStatus;
}
