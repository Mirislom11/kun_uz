package com.company.repository;

import com.company.entity.LikeEntity;
import com.company.enums.LikeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
    @Modifying
    @Transactional
    void deleteAllById(int id);
    @Query("SELECT COUNT(l) FROM LikeEntity l WHERE l.id = ?1")
    Long getCountOfLikeByArticleEntityId(int articleId);
    @Query("SELECT l FROM LikeEntity l where l.profile.id = ?1 and l.status = ?2")
    List<LikeEntity> getMyLikeOrDislikeList(int profileId, LikeStatus likeStatus);
    @Query("SELECT l FROM LikeEntity l WHERE l.profile.id = ?1 AND l.articleEntity.id = ?2")
    Optional<LikeEntity> getLikeEntityProfileAndAticleId(int profileId, int articleId);
}
