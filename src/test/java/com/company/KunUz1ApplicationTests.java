package com.company;

import com.company.dto.ArticleDTO;
import com.company.dto.ArticleFilterDTO;
import com.company.dto.comment.CommentFilterDTO;
import com.company.dto.profile.ProfileFilterDTO;
import com.company.enums.ProfileRole;
import com.company.repository.ArticleCustomRepositoryImpl;
import com.company.service.CommentService;
import com.company.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class KunUz1ApplicationTests {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleCustomRepositoryImpl articleCustomRepository;
    @Test
    void createProfile() {
    }
    @Test
    public void createArticle() {
  /*      ProfileFilterDTO dto = new ProfileFilterDTO();
        dto.setName("mi");
        dto.setLogin("mi");
        dto.setProfileRole(ProfileRole.USER_ROLE);
        Pageable pageable = profileService.filter(1, 1, dto).getPageable();
        System.out.println(pageable);*/
/*        CommentFilterDTO dto = new CommentFilterDTO();
        dto.setContent("No");
        commentService.filter(1, 1, dto);*/
        ArticleFilterDTO articleFilterDTO = new ArticleFilterDTO();
        articleFilterDTO.setTitle("Olimp");
        articleCustomRepository.filterCriteriaBuilder(articleFilterDTO);
    }

}
