package com.company.repository;

import com.company.dto.ArticleFilterDTO;
import com.company.entity.ArticleEntity;
import com.company.enums.ArticleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.script.ScriptEngine;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ArticleCustomRepositoryImpl {
    @Autowired
    private EntityManager entityManager;

    public PageImpl filter(int page, int size, ArticleFilterDTO filterDTO) {
        Map<String, Object> params = new HashMap<>();

        StringBuilder builder = new StringBuilder("SELECT a FROM ArticleEntity a ");
        StringBuilder builderCount = new StringBuilder("SELECT count(a) FROM ArticleEntity a ");

        if (filterDTO.getStatus() != null) {
            builder.append("Where status ='" + filterDTO.getStatus().name() + "'");
            builderCount.append("Where status ='" + filterDTO.getStatus().name() + "'");
        } else {
            builder.append("Where status ='PUBLISHED'");
            builderCount.append("Where status ='PUBLISHED'");
        }

        if (filterDTO.getArticleId() != null) {
            builder.append(" and a.id =:id");
            builderCount.append(" and a.id =:id");
            params.put("id", filterDTO.getArticleId());
        }
        if (filterDTO.getTitle() != null && !filterDTO.getTitle().isEmpty()) {
            builder.append(" and a.title =:title");
            builderCount.append(" and a.title =:title");
            params.put("title", filterDTO.getTitle());
        }

        if (filterDTO.getProfileId() != null) {
            builder.append(" and a.profile.id =:profileId");
            builderCount.append(" and a.profile.id =:profileId");
            params.put("profileId", filterDTO.getProfileId());
        }
        if (filterDTO.getFromDate() != null) {
            builder.append(" and a.createdDate >=:fromDate");
            builderCount.append(" and a.createdDate >=:fromDate");
            params.put("fromDate", LocalDateTime.of(filterDTO.getFromDate(), LocalTime.MIN));
        }

        if (filterDTO.getToDate() != null) {
            builder.append(" and a.createdDate <=:toDate");
            builderCount.append(" and a.createdDate <=:toDate");
            params.put("toDate", LocalDateTime.of(filterDTO.getToDate(), LocalTime.MAX));
        }

        Query query = entityManager.createQuery(builder.toString());
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        for (Map.Entry<String, Object> entrySet : params.entrySet()) {
            query.setParameter(entrySet.getKey(), entrySet.getValue());
        }
        List<ArticleEntity> articleList = query.getResultList();


        query = entityManager.createQuery(builderCount.toString());
        for (Map.Entry<String, Object> entrySet : params.entrySet()) {
            query.setParameter(entrySet.getKey(), entrySet.getValue());
        }
        Long totalCount = (Long) query.getSingleResult();

        return new PageImpl(articleList, PageRequest.of(page, size), totalCount);
    }

    public void filterCriteriaBuilder(ArticleFilterDTO articleFilterDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ArticleEntity> criteriaQuery = criteriaBuilder.createQuery(ArticleEntity.class);
        Root<ArticleEntity> root = criteriaQuery.from(ArticleEntity.class);
        List<Predicate> predicateList = new ArrayList<>();
        criteriaQuery.select(root);
        /*Predicate namePredicate = criteriaBuilder.like(root.get("title"), articleFilterDTO.getTitle() + "%");
        criteriaQuery.where(namePredicate);
        List<ArticleEntity> articleEntityList = entityManager.createQuery(criteriaQuery).getResultList();
        System.out.println(articleEntityList);*/
        if (articleFilterDTO.getArticleId() != null) {
            predicateList.add(criteriaBuilder.equal(root.get("article.id"), articleFilterDTO.getArticleId()));
        }
        if (articleFilterDTO.getStatus() != null) {
            predicateList.add(criteriaBuilder.equal(root.get("status"), articleFilterDTO.getStatus()));
        }/*else {
            predicateList.add(criteriaBuilder.equal(root.get("status"), ArticleStatus.valueOf("PUBLISHED")));
        }*/
        if (articleFilterDTO.getTitle() != null) {
            predicateList.add(criteriaBuilder.like(root.get("title"), articleFilterDTO.getTitle() + "%"));
        }
        if (articleFilterDTO.getProfileId() != null){
            predicateList.add(criteriaBuilder.equal(root.get("profile.id"), articleFilterDTO.getTitle()));
        }
        if (articleFilterDTO.getFromDate() != null) {
            predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDateTime"), LocalDateTime.of(articleFilterDTO.getFromDate(), LocalTime.MIN)));
        }
        if (articleFilterDTO.getToDate() != null) {
            predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDateTime"), LocalDateTime.of(articleFilterDTO.getToDate(), LocalTime.MAX)));
        }
/*        for (Predicate predicate : predicateList) {
            criteriaQuery.where(predicate);
        }*/
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        criteriaQuery.where(predicates);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));

        List<ArticleEntity> articleEntityList = entityManager.createQuery(criteriaQuery).getResultList();
        System.out.println(articleEntityList);
    }

}
