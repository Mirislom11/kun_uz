package com.company.service;

import com.company.dto.ArticleDTO;
import com.company.dto.ArticleFilterDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.RegionEntity;
import com.company.enums.ArticleStatus;
import com.company.enums.ArticleThemes;
import com.company.exception.BadRequestException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.ArticleCustomRepositoryImpl;
import com.company.repository.ArticleRepository;
import com.company.spec.ArticleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.swing.text.html.HTMLDocument;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private RegionService regionService;
    @Autowired
    ProfileService profileService;
    @Autowired
    private ArticleCustomRepositoryImpl articleCustomRepository;

    public ArticleDTO create(ArticleDTO dto, Integer userId) {
        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new BadRequestException("Title can not be null");
        }
        if (dto.getContent() == null || dto.getContent().isEmpty()) {
            throw new BadRequestException("Content can not be null");
        }
        RegionEntity region = regionService.getRegionEntity(dto.getRegionId());
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setTitle(dto.getTitle());
        entity.setCreatedDateTime(LocalDateTime.now());
        entity.setModerator(profileService.get(userId));
        entity.setRegion(region);
        entity.setStatus(ArticleStatus.CREATED);
        entity.setThemes(dto.getArticleThemes());
        articleRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDateTime());
        return dto;
    }
    public List<ArticleDTO> publish (String title, Integer publisherId) {
        ProfileEntity publisher = profileService.get(publisherId);
        List<ArticleDTO> articleDTOList = new LinkedList<>();
        List<ArticleEntity> articleEntityList = articleRepository.findAllByTitleLike(title+"%");
        for (ArticleEntity articleEntity : articleEntityList) {
            articleEntity.setPublisher(publisher);
            articleEntity.setStatus(ArticleStatus.PUBLISHED);
            articleEntity.setPublishedDate(LocalDateTime.now());
            articleRepository.save(articleEntity);
            articleDTOList.add(toDTO(articleEntity));
        }
        if (articleDTOList.isEmpty()){
            throw new ItemNotFoundException("Article not found");
        }
        return articleDTOList;

    }

    public List<ArticleDTO> blocked (String title) {
        List<ArticleEntity> articleEntityList = articleRepository.findAllByTitleLike(title+"%");
        List<ArticleDTO> articleDTOList = new LinkedList<>();
        for (ArticleEntity articleEntity : articleEntityList) {
            articleEntity.setStatus(ArticleStatus.BLOCKED);
            articleRepository.save(articleEntity);
            articleDTOList.add(toDTO(articleEntity));
        }
        if (articleDTOList.isEmpty()) {
            throw new ItemNotFoundException("Article by this id not found ");
        }
        return articleDTOList;

    }

    public List<ArticleDTO> getAllArticle () {
        List<ArticleEntity> allArticleList = articleRepository.getAllArticleSortedByCreatedDateTime();
        List<ArticleDTO> articleDTOList = new LinkedList<>();
        for (ArticleEntity articleEntity : allArticleList) {
            articleDTOList.add(toDTO(articleEntity));
        }
        return articleDTOList;
    }

    public List<ArticleDTO> getAllArticleByArticleTheme (ArticleThemes articleThemes) {
        List<ArticleEntity> articleEntityList = articleRepository.findAllByThemesOrderByCreatedDateTime(articleThemes);
        List<ArticleDTO> articleDTOList = new LinkedList<>();
        for (ArticleEntity articleEntity : articleEntityList) {
            articleDTOList.add(toDTO(articleEntity));
        }
        return articleDTOList;
    }

    public List<ArticleDTO> getAllArtilceByRegion (int regionId){
        RegionEntity regionEntity = regionService.getRegionEntity(regionId);
        if (regionEntity == null) {
            throw new ItemNotFoundException("Region by this id not found");
        }
        List<ArticleDTO> articleDTOList = new LinkedList<>();
        List<ArticleEntity> articleEntityList = articleRepository.findAllByRegionOrderByCreatedDateTime(regionEntity);
        for (ArticleEntity articleEntity : articleEntityList) {
            articleDTOList.add(toDTO(articleEntity));
        }
        return articleDTOList;
    }
    public List<ArticleDTO> getAllArticleByCreatedDate (LocalDate localDate) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        List<ArticleEntity> articleEntityList = articleRepository.findAll(sort);
        List<ArticleDTO> articleDTOList = new LinkedList<>();
        for (ArticleEntity articleEntity : articleEntityList) {
            if (articleEntity.getCreatedDateTime().toLocalDate().equals(localDate)) {
                articleDTOList.add(toDTO(articleEntity));
            }
        }
        return articleDTOList;
    }
    public ArticleEntity getArticleEntity(Integer id) {
        Optional<ArticleEntity> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            return optionalArticle.get();
        }
        throw new ItemNotFoundException("Article by this id not found");
    }

    public PageImpl<ArticleDTO> filter(int page, int size, ArticleFilterDTO filterDTO) {
        PageImpl<ArticleEntity> entityPage = articleCustomRepository.filter(page, size, filterDTO);

        List<ArticleDTO> articleDTOList = entityPage.stream().map(articleEntity -> {
            ArticleDTO dto = new ArticleDTO();
            dto.setId(articleEntity.getId());
            //
            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(articleDTOList, entityPage.getPageable(), entityPage.getTotalElements());
    }
    /*9. Filter with specification
    1. Article filter: articleId, profileId, status, createdDate (fromDate, toDate),
            Pagination, orderByFiled [asc,desc]*/

    public PageImpl<ArticleDTO> filterSpe(int page, int size, ArticleFilterDTO filterDTO) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Specification<ArticleEntity> spec = null;
        if (filterDTO.getStatus() != null) {
            spec = Specification.where(ArticleSpecification.status(filterDTO.getStatus()));
        } else {
            spec = Specification.where(ArticleSpecification.status(ArticleStatus.PUBLISHED));
        }

        if (filterDTO.getTitle() != null) {
            spec.and(ArticleSpecification.title(filterDTO.getTitle()));
        }
        if (filterDTO.getArticleId() != null) {
            spec.and(ArticleSpecification.equal("id", filterDTO.getArticleId()));
        }
        if (filterDTO.getProfileId() != null) {
            spec.and(ArticleSpecification.equal("profile.id", filterDTO.getProfileId()));
        }
        if (filterDTO.getFromDate() != null) {
            spec.and(ArticleSpecification.fromDate(filterDTO.getFromDate()));
        }
        if (filterDTO.getToDate() != null) {
            spec.and(ArticleSpecification.toDate(filterDTO.getToDate()));
        }
        Page<ArticleEntity> entityPage = articleRepository.findAll(spec, pageable);
        List<ArticleEntity> articleEntityList = entityPage.getContent();
        List<ArticleDTO> articleDTOList = new LinkedList<>();
        for (ArticleEntity articleEntity : articleEntityList) {
            articleDTOList.add(toDTO(articleEntity));
        }
        return new PageImpl<>(articleDTOList, pageable, entityPage.getTotalElements());
    }




    private ArticleDTO toDTO (ArticleEntity articleEntity) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(articleEntity.getId());
        articleDTO.setProfileId(articleEntity.getModerator().getId());
        articleDTO.setPublishedDate(articleEntity.getPublishedDate());
        articleDTO.setTitle(articleEntity.getTitle());
        articleDTO.setContent(articleEntity.getContent());
        articleDTO.setArticleThemes(articleEntity.getThemes());
        articleDTO.setRegionId(articleEntity.getRegion().getId());
        articleDTO.setCreatedDate(articleEntity.getCreatedDateTime());
        return articleDTO;

    }


}
