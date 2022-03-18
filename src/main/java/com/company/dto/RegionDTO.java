package com.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class RegionDTO {
   private long regionId;
   private String country;
   private String city;
   private String area;
   private LocalDateTime createdDate;
}
