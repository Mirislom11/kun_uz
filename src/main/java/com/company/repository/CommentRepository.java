package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.entity.ProfileEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer>, JpaSpecificationExecutor<CommentEntity> {
    List<CommentEntity> findAllByProfileEntity(ProfileEntity profile, Sort sort);
    List<CommentEntity> findAllByArticleEntity(ArticleEntity articleEntity, Sort sort);
    @Transactional
    @Modifying
    void deleteByProfileEntityAndId(ProfileEntity profile, int id);
}
