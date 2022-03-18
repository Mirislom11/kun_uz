package com.company.repository;

import com.company.entity.ArticleEntity;
import com.company.entity.RegionEntity;
import com.company.enums.ArticleThemes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Integer>, JpaSpecificationExecutor<ArticleEntity> {
    @Query("SELECT a FROM ArticleEntity  a ORDER BY a.createdDateTime")
    List<ArticleEntity> getAllArticleSortedByCreatedDateTime();
    List<ArticleEntity> findAllByThemesOrderByCreatedDateTime(ArticleThemes themes);
    List<ArticleEntity> findAllByRegionOrderByCreatedDateTime(RegionEntity region);
    List<ArticleEntity> findAllByTitleLike(String title);
}
