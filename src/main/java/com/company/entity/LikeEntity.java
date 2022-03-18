package com.company.entity;

import com.company.enums.LikeStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

// id,articleId,ProfileId,createdDate,status(like,dislike)
@Getter
@Setter
@Entity
@Table(name = "article_likes")
public class LikeEntity extends BasedEntity{
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LikeStatus status;
    @Enumerated(EnumType.STRING)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "profile_id")
    private ProfileEntity profile;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "article_id")
    private ArticleEntity articleEntity;
}
