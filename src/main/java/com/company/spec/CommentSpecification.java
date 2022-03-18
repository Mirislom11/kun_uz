package com.company.spec;

import com.company.entity.CommentEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CommentSpecification {

    public static Specification<CommentEntity> content(String content){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("content"), "%"+ content + "%"));
    }
    public static Specification<CommentEntity> equal(String requiredRoot, int id) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(requiredRoot), id));
    }
    public static Specification<CommentEntity> fromDate(LocalDate fromDate) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("createdDateTime"), LocalDateTime.of(fromDate, LocalTime.MIN)));
    }
    public static Specification<CommentEntity> toDate(LocalDate toDate) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("createdDateTime"), LocalDateTime.of(toDate, LocalTime.MAX)));
    }

}
