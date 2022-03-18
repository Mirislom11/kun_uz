package com.company.service;

import com.company.dto.LikeDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.LikeEntity;
import com.company.enums.LikeStatus;
import com.company.exception.ForbiddenException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/*2. Delete like/dislike
    3. Update like/dislike*/

/*4. Get article like and dislike count
    5. Get comment like and dislike count*/
/* 6. Get profile liked article list
    7. Get profile liked comment list*/

@Service
public class ArticleLikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ProfileService profileService;

    public LikeDTO createLike (LikeDTO likeDTO, Integer profileId) {
        ArticleEntity article = articleService.getArticleEntity(likeDTO.getArticleId());
        Optional<LikeEntity> optional =likeRepository.getLikeEntityProfileAndAticleId(profileId, likeDTO.getArticleId());
        optional.ifPresent(likeEntity -> updateLike(likeDTO, profileId, likeEntity.getId()));
        LikeEntity likeEntity = new LikeEntity();
        likeEntity.setCreatedDateTime(LocalDateTime.now());
        likeEntity.setProfile(profileService.get(profileId));
        likeEntity.setStatus(likeDTO.getLikeStatus());
        likeEntity.setArticleEntity(article);
        likeRepository.save(likeEntity);
        likeDTO.setId(likeEntity.getId());
        likeDTO.setCreatedDateTime(likeEntity.getCreatedDateTime());
        likeDTO.setProfileId(likeEntity.getProfile().getId());
        return likeDTO;
    }

    public String deleteLikeById(int likeId, Integer profileId) {

        if (!profileId.equals(get(likeId).getProfile().getId())) {
            throw new ForbiddenException("You not owner this like");
        }
        likeRepository.deleteAllById(likeId);
        return "Successfully deleted";
    }
    public String updateLike (LikeDTO likeDTO, Integer profileId,int likeId) {
        if (!profileId.equals(get(likeId).getProfile().getId())) {
            throw new ForbiddenException("You not owner this like");
        }
        LikeEntity likeEntity = get(likeId);
        likeEntity.setStatus(likeDTO.getLikeStatus());
        likeRepository.save(likeEntity);
        return "Successfully updated";
    }

    public LikeDTO getLikeById (int id) {
        LikeEntity like = get(id);
        LikeDTO likeDTO = toDTO(like);
        return likeDTO;
    }
    public long getCountOfLikeByArticle(int articleId){
        return  likeRepository.getCountOfLikeByArticleEntityId(articleId);
    }
    public List<LikeDTO>   getAllMyLikeOrDislike(int profileId, LikeStatus status) {
        List<LikeEntity> likeEntities = likeRepository.getMyLikeOrDislikeList(profileId, status);
        List<LikeDTO> likeDTOList = new LinkedList<>();
        for (LikeEntity likeEntity : likeEntities) {
            likeDTOList.add(toDTO(likeEntity));
        }
        return likeDTOList;
    }

    private void checkArticle(Integer articleId){
        ArticleEntity articleEntity = articleService.getArticleEntity(articleId);
        if (articleEntity == null) {
            throw new ItemNotFoundException("Article by this id not found");
        }
    }
    private LikeDTO toDTO (LikeEntity like) {
        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setId(like.getId());
        likeDTO.setLikeStatus(like.getStatus());
        likeDTO.setProfileId(like.getProfile().getId());
        likeDTO.setCreatedDateTime(like.getCreatedDateTime());
        return likeDTO;
    }

    public LikeEntity get(int likeId) {
        Optional<LikeEntity> optional = likeRepository.findById(likeId);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new ItemNotFoundException("Like by this id not found");
    }
}
