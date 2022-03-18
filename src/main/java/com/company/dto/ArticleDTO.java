package com.company.dto;

import com.company.enums.ArticleThemes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private Integer id;
    @NotEmpty(message = "title null or empty")
    private String title;
    @NotBlank(message = "content null or empty")
    private String content;
    @Positive(message = "profile id must be positive")
    private Integer profileId;
    @Positive(message = "article id must be positive")
    private ArticleThemes articleThemes;
    private int  regionId;
    // status

    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;

}
