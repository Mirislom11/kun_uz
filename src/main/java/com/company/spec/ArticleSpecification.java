package com.company.spec;

import com.company.entity.ArticleEntity;
import com.company.enums.ArticleStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ArticleSpecification {
    public static Specification<ArticleEntity> status(ArticleStatus status) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));
    }

    public static Specification<ArticleEntity> title(String title) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("title"), title));
    }

    public static Specification<ArticleEntity> equal(String field, Integer id) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), id));
    }
    public static Specification<ArticleEntity> fromDate(LocalDate localDate){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdDateTime"), LocalDateTime.of(localDate, LocalTime.MIN)));
    }
    public static Specification<ArticleEntity> toDate(LocalDate toDate) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("createdDateTime"), LocalDateTime.of(toDate, LocalTime.MAX)));
    }
}
