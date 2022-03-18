package com.company.dto.comment;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
public class CommentFilterDTO {
    private String content;
    private Integer articleId;
    private Integer profileId;
    private LocalDate fromDate;
    private LocalDate toDate;

}
