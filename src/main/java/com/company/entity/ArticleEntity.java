package com.company.entity;

import com.company.enums.ArticleStatus;
import com.company.enums.ArticleThemes;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "article")
public class ArticleEntity extends BasedEntity{
    @Column
    private String title;
    @Column
    private String content;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private ProfileEntity publisher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id")
    private ProfileEntity moderator;
    @Column(name = "article_status")
    @Enumerated(EnumType.STRING)
    private ArticleStatus status;
    @Enumerated(EnumType.STRING)
    private ArticleThemes themes;
    @JoinColumn(name = "region_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private RegionEntity region;
    @Column
    private Long likes;
    @Column
    private Long disLike;


}
