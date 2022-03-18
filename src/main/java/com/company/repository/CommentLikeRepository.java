package com.company.repository;

import com.company.entity.CommentLikeEntity;
import com.company.enums.LikeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.JoinColumn;
import java.util.List;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, Integer> {
    @Query("SELECT l FROM CommentLikeEntity l WHERE l.comment.id = ?1 AND l.likeStatus = ?2")
    List<CommentLikeEntity> getCommentLikeByCommentIdLAndLikeStatus(int commentId, LikeStatus status);
    @Query("SELECT l FROM CommentLikeEntity l WHERE l.profile.id = ?1 AND  l.likeStatus = ?2")
    List<CommentLikeEntity> getCommentLikeByProfileId(int id, LikeStatus likeStatus);

    @Override
    void deleteById(Integer integer);
}
