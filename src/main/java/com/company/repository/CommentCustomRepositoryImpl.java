package com.company.repository;

import com.company.dto.comment.CommentFilterDTO;
import com.company.entity.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentCustomRepositoryImpl {
    @Autowired
    private EntityManager entityManager;

    public PageImpl filter (int page, int size, CommentFilterDTO commentFilterDTO) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder("SELECT c FROM CommentEntity c");
        StringBuilder builderCount = new StringBuilder("SELECT count(c) FROM CommentEntity c");
        builder.append(" WHERE content like '"+ commentFilterDTO.getContent() +"%" + "'");
        builderCount.append(" WHERE content like '"+ commentFilterDTO.getContent() +"%" + "'");

        if (commentFilterDTO.getArticleId() != null) {
            builder.append(" AND article.id = :articleId");
            builderCount.append(" AND article.id = :articleId");
            params.put("articleId", commentFilterDTO.getArticleId());
        }
        if (commentFilterDTO.getProfileId() != null) {
            builder.append(" AND profile.id = :profileId");
            builderCount.append(" AND profile.id = :profileId");
            params.put("profileId", commentFilterDTO.getProfileId());
        }
        if(commentFilterDTO.getFromDate() != null) {
            builder.append(" AND createdDateTime >= :fromDateTime");
            builderCount.append(" AND createdDateTime >= fromDateTime");
            params.put("createdDateTime", LocalDateTime.of(commentFilterDTO.getFromDate(), LocalTime.MIN));
        }
        if (commentFilterDTO.getToDate() != null) {
            builder.append(" AND createdDateTime <= :toDate");
            builderCount.append(" AND createdDateTime <= toDate");
            params.put("createdDateTime", LocalDateTime.of(commentFilterDTO.getToDate(), LocalTime.MAX));
        }

        Query query = entityManager.createQuery(builder.toString());
        query.setFirstResult((page-1) * size);
        query.setMaxResults(size);
        for (Map.Entry<String, Object> entrySet : params.entrySet()) {
            query.setParameter(entrySet.getKey(), entrySet.getValue());
        }
        List<CommentEntity> commentEntityList = query.getResultList();


        query = entityManager.createQuery(builderCount.toString());
        for (Map.Entry<String, Object> entrySet : params.entrySet()) {
            query.setParameter(entrySet.getKey(), entrySet.getValue());
        }
        long totalCount = (long) query.getSingleResult();
        return new PageImpl(commentEntityList, PageRequest.of(page, size), totalCount);
    }

}
