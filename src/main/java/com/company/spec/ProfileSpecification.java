package com.company.spec;

import com.company.entity.ProfileEntity;
import com.company.enums.ProfileRole;
import org.springframework.data.jpa.domain.Specification;

import java.security.SecurityPermission;

public class ProfileSpecification {

    public static Specification<ProfileEntity> role(ProfileRole profileRole) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"), profileRole));
    }
    public static Specification<ProfileEntity> stringEqual(String requiredRoot, String value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(requiredRoot), value));
    }
    public static Specification<ProfileEntity> nameAndSurnameLike(String requiredRoot, String value) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(requiredRoot), value));
    }
}
