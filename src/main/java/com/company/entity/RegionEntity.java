package com.company.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.mapping.Join;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "region")
public class RegionEntity extends BasedEntity{
    @Column(name = "country", nullable = false, length = 32)
    private String country;
    @Column(name = "city", nullable = false, length = 32)
    private String city;
    @Column(name = "area", nullable = false, length = 32)
    private String area;

}
