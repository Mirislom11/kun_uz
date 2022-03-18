package com.company.repository;

import com.company.dto.profile.ProfileDTO;
import com.company.dto.profile.ProfileFilterDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository

public class ProfileCustomRepositoryImpl {
    @Autowired
    private EntityManager entityManager;

    public PageImpl filter (int page, int size, ProfileFilterDTO profileFilterDTO) {
        Map<String, Object> params = new HashMap();
        StringBuilder builder = new StringBuilder("SELECT p FROM ProfileEntity p");
        StringBuilder builderCounter= new StringBuilder("SELECT count(p) FROM ProfileEntity p");

        if (profileFilterDTO.getProfileRole() != null) {
            builder.append(" WHERE profileRole = '"+ profileFilterDTO.getProfileRole()+"'");
            builderCounter.append(" WHERE profileRole = '"+ profileFilterDTO.getProfileRole()+"'");
        }else {
            builder.append(" WHERE profileRole = USER_ROLE");
            builderCounter.append(" WHERE profileRole = USER_ROLE");
        }
        if (profileFilterDTO.getName() != null) {
            builder.append(" AND name like :name");
            builderCounter.append(" AND name like :name");
            params.put("name", profileFilterDTO.getName()+"%");
        }
        if (profileFilterDTO.getSurname() != null) {
            builder.append(" AND surname like :surname");
            builderCounter.append(" AND surname like :surname");
            params.put("surname", profileFilterDTO.getName()+"%");
        }
        if (profileFilterDTO.getLogin() != null) {
            builder.append(" AND login like :login");
            builderCounter.append(" AND login like :login");
            params.put("login", profileFilterDTO.getLogin() + "%");
        }
        if (profileFilterDTO.getEmail() != null) {
            builder.append(" AND email like :email");
            builderCounter.append(" AND email like :email");
            params.put("email", profileFilterDTO.getEmail() + "%");
        }
        if (profileFilterDTO.getPassword() != null) {
            builder.append(" AND password = :password");
            builder.append(" AND password = :password");
            params.put("password", profileFilterDTO.getPassword());
        }
        Query query = entityManager.createQuery(builder.toString());
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        for (Map.Entry<String, Object> entrySet : params.entrySet()) {
            query.setParameter(entrySet.getKey(), entrySet.getValue());
        }
        List<ProfileEntity> profileEntityList = query.getResultList();

        query = entityManager.createQuery(builderCounter.toString());
        for (Map.Entry<String, Object> entrySet : params.entrySet()) {
            query.setParameter(entrySet.getKey(), entrySet.getValue());
        }
        Long totalCount = (Long) query.getSingleResult();

        return new PageImpl(profileEntityList, PageRequest.of(page, size), totalCount);
    }
}
