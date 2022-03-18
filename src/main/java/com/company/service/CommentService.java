package com.company.service;

import com.company.dto.comment.CommentDTO;
import com.company.dto.comment.CommentFilterDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.CommentEntity;
import com.company.entity.ProfileEntity;
import com.company.exception.BadRequestException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.CommentCustomRepositoryImpl;
import com.company.repository.CommentRepository;
import com.company.spec.ArticleSpecification;
import com.company.spec.CommentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*    1. Create
    3. Get by id
        ** Authorized and owner **
    2. Update
    4. Delete
        ** All **
    5. Get Profile Comment list
    6. Get Article Comment list (pagination)
        ** Admin **
    7. Get all comment list (with pagination)*/


@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentCustomRepositoryImpl commentCustomRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ArticleService articleService;
    public CommentDTO createComment(CommentDTO commentDTO, Integer profileId) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(commentDTO.getContent());
        ProfileEntity profile = profileService.get(profileId);
        commentEntity.setProfileEntity(profile);
        ArticleEntity articleEntity = articleService.getArticleEntity(commentDTO.getArticleId());
        checkArticle(articleEntity);
        commentEntity.setArticleEntity(articleEntity);
        commentEntity.setCreatedDateTime(LocalDateTime.now());
        commentRepository.save(commentEntity);
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCreatedDateTime(commentEntity.getCreatedDateTime());
        return commentDTO;
    }

    public List<CommentDTO>     getMyCommentList(int profileId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDateTime");
        ProfileEntity profile = profileService.get(profileId);
        List<CommentEntity> myCommentList = commentRepository.findAllByProfileEntity(profile, sort);
        List<CommentDTO >commentDTOList = new LinkedList<>();
        for (CommentEntity commentEntity : myCommentList) {
            commentDTOList.add(toDTO(commentEntity));
        }
        return commentDTOList;
    }

    public List<CommentDTO> getAllComments() {
        List<CommentEntity> commentEntityList = commentRepository.findAll();
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            commentDTOList.add(toDTO(commentEntity));
        }
        return commentDTOList;
    }

    public List<CommentDTO> getAllCommentsByArticleId(int articleId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDateTime");
        ArticleEntity articleEntity = articleService.getArticleEntity(articleId);
        checkArticle(articleEntity);
        List<CommentEntity> commentEntityList = commentRepository.findAllByArticleEntity(articleEntity, sort);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            commentDTOList.add(toDTO(commentEntity));
        }
        return commentDTOList;
    }

    public String deleteMyComment (int commentId, int profileId) {
        ProfileEntity profile = profileService.get(profileId);
        commentRepository.deleteByProfileEntityAndId(profile, commentId);
        return "Successfully deleted";
    }
    public String updateMyComment(int  commentId, int profileId, CommentDTO commentDTO) {
        ProfileEntity profile = profileService.get(profileId);
        Optional<CommentEntity> optionalProfile = commentRepository.findById(commentId);
        if (optionalProfile.isPresent()) {
            CommentEntity commentEntity = optionalProfile.get();
            if (!commentEntity.getProfileEntity().equals(profile)) {
                throw new BadRequestException("This user dont write this comment");
            }
            commentEntity.setContent(commentDTO.getContent());
            commentRepository.save(commentEntity);
            return "Successfully updated";
        }
        throw new ItemNotFoundException("Comment by this id not found");
    }
    public PageImpl<CommentDTO> filter(int page, int size, CommentFilterDTO commentFilterDTO) {
        PageImpl<CommentEntity> entityPage = commentCustomRepository.filter(page, size, commentFilterDTO);
        List<CommentDTO> commentDTOList =  entityPage.stream().map(entity ->{
            CommentDTO commentDTO = toDTO(entity);
            return commentDTO;
        }).collect(Collectors.toList());
        return new PageImpl<>(commentDTOList, entityPage.getPageable(), entityPage.getTotalElements());
    }

    public PageImpl<CommentDTO> filterSpec(int page, int size, CommentFilterDTO filterDTO) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDateTime"));
        Specification<CommentEntity> specification = null;
        specification = Specification.where(CommentSpecification.content(filterDTO.getContent()));

        if (filterDTO.getProfileId() != null) {
            specification.and(CommentSpecification.equal("profileEntity.id", filterDTO.getProfileId()));
        }
        if(filterDTO.getArticleId() != null) {
            specification.and(CommentSpecification.equal("articleEntity.id", filterDTO.getArticleId()));
        }
        if (filterDTO.getToDate() != null) {
            specification.and(CommentSpecification.toDate(filterDTO.getToDate()));
        }
        if (filterDTO.getFromDate() != null) {
            specification.and(CommentSpecification.fromDate(filterDTO.getFromDate()));
        }
        Page<CommentEntity> entityPage = commentRepository.findAll(specification, pageable);
        List<CommentEntity> commentEntityList = entityPage.getContent();
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            commentDTOList.add(toDTO(commentEntity));
        }
        return new PageImpl<>(commentDTOList, pageable, entityPage.getTotalElements());
    }

    private CommentDTO toDTO (CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setProfileName(commentEntity.getProfileEntity().getName());
        commentDTO.setArticleName(commentEntity.getArticleEntity().getContent());
        commentDTO.setContent(commentEntity.getContent());
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCreatedDateTime(commentEntity.getCreatedDateTime());
        return commentDTO;
    }


    private boolean checkArticle(ArticleEntity articleEntity){
        if (articleEntity == null) {
            throw new ItemNotFoundException("Article is null");
        }
        return true;
    }

    public CommentEntity get(int id) {
        Optional<CommentEntity> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            return optionalComment.get();
        }
        throw new ItemNotFoundException("Comment Entity not found");
    }
}
