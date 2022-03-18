package com.company.entity;

import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CollectionId;

import javax.persistence.*;
import java.time.LocalDateTime;


@Setter
@Getter
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String login;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "profile_role")
    @Enumerated(EnumType.STRING)
    private ProfileRole profileRole;
    @Column(name = "last_active_date")
    private LocalDateTime lastActiveDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;
}
