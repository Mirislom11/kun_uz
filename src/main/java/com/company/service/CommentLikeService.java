package com.company.service;

import com.company.dto.CommentLikeDTO;
import com.company.dto.LikeDTO;
import com.company.entity.CommentEntity;
import com.company.entity.CommentLikeEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.LikeStatus;
import com.company.exception.ItemNotFoundException;
import com.company.repository.CommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.sax.SAXResult;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentLikeService {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    public CommentLikeDTO createLikeComment(CommentLikeDTO commentLikeDTO, int profileId){
        ProfileEntity profile = profileService.get(profileId);
        CommentEntity comment = commentService.get(commentLikeDTO.getCommentId());
        checkProfile(profile);
        checkComment(comment);
        CommentLikeEntity commentLikeEntity = new CommentLikeEntity();
        commentLikeEntity.setProfile(profile);
        commentLikeEntity.setComment(comment);
        commentLikeEntity.setCreatedDateTime(LocalDateTime.now());
        commentLikeEntity.setLikeStatus(commentLikeDTO.getLikeStatus());
        commentLikeRepository.save(commentLikeEntity);
        commentLikeDTO.setId(commentLikeEntity.getId());
        commentLikeDTO.setCreatedDateTime(commentLikeEntity.getCreatedDateTime());
        commentLikeDTO.setProfileId(commentLikeEntity.getProfile().getId());
        return commentLikeDTO;
    }

    public CommentLikeDTO getCommentLikeById(int id) {
        CommentLikeEntity commentLike = get(id);
        return toDTO(commentLike);
    }

    public List<CommentLikeDTO> getListOfCommentLike(){
        List<CommentLikeEntity> commentLikeEntityList = commentLikeRepository.findAll();
        List<CommentLikeDTO> commentLikeDTOList = new LinkedList<>();
        for (CommentLikeEntity commentLikeEntity : commentLikeEntityList) {
            commentLikeDTOList.add(toDTO(commentLikeEntity));
        }
        return commentLikeDTOList;
    }
    public List<CommentLikeDTO> getCommentLikeDTOByCommentId(LikeStatus likeStatus, int commentId) {
        List<CommentLikeEntity> commentLikeEntityList = commentLikeRepository.getCommentLikeByCommentIdLAndLikeStatus( commentId, likeStatus);
        List<CommentLikeDTO> commentLikeDTOList = new ArrayList<>();
        for (CommentLikeEntity commentLikeEntity : commentLikeEntityList) {
            commentLikeDTOList.add(toDTO(commentLikeEntity));
        }
        return commentLikeDTOList;
    }
    public List<CommentLikeDTO> getCommentLikDTOByProfileId(int profileId, LikeStatus likeStatus) {
        List<CommentLikeEntity> commentLikeEntityList = commentLikeRepository.getCommentLikeByProfileId(profileId, likeStatus);
        List<CommentLikeDTO> commentLikeDTOList = new ArrayList<>();
        for (CommentLikeEntity entity : commentLikeEntityList) {
            commentLikeDTOList.add(toDTO(entity));
        }
        return commentLikeDTOList;
    }
    public String updateCommentLikeById (int commentLikeId, CommentLikeDTO commentLikeDTO) {
         Optional<CommentLikeEntity> optional = commentLikeRepository.findById(commentLikeId);
        if (optional.isPresent()) {
            CommentLikeEntity entity = optional.get();
            entity.setLikeStatus(commentLikeDTO.getLikeStatus());
        }
        return "Successfully updated";
    }
    public String deleteCommentLikeById(int commentLikeId) {
        deleteCommentLikeById(commentLikeId);
        return "Successfully updated";
    }
    private CommentLikeDTO toDTO(CommentLikeEntity entity){
        CommentLikeDTO commentLikeDTO = new CommentLikeDTO();
        commentLikeDTO.setId(entity.getId());
        commentLikeDTO.setLikeStatus(entity.getLikeStatus());
        commentLikeDTO.setCommentId(entity.getComment().getId());
        commentLikeDTO.setProfileId(entity.getProfile().getId());
        commentLikeDTO.setCreatedDateTime(entity.getCreatedDateTime());
        return commentLikeDTO;
    }
    private void checkComment(CommentEntity comment) {
        if(comment == null) {
            throw new ItemNotFoundException("Comment by this id not found");
        }
    }
    private void checkProfile (ProfileEntity profile) {
        if (profile == null) {
            throw new ItemNotFoundException("Profile by this id not foud");
        }
    }

    public CommentLikeEntity get(int id) {
        Optional<CommentLikeEntity> optional = commentLikeRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new ItemNotFoundException("Like of comment by this id not found");
    }
}
