package com.company.controller;

import com.company.dto.ArticleDTO;
import com.company.dto.ArticleFilterDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.enums.ArticleThemes;
import com.company.enums.ProfileRole;
import com.company.service.ArticleService;
import com.company.util.JwtUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PostMapping("/create")
    public ResponseEntity<ArticleDTO> create(@Valid @RequestBody ArticleDTO dto, HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request, ProfileRole.MODERATOR_ROLE);
        ArticleDTO articleDTO = articleService.create(dto, profileJwtDTO.getId());
        return ResponseEntity.ok(articleDTO);
    }

    @GetMapping("/get-ALL")
    public ResponseEntity<?> getAllArticle() {
        List<ArticleDTO> articleDTOList = articleService.getAllArticle();
        return ResponseEntity.ok(articleDTOList);
    }
    @GetMapping("/get-All-article-by-theme")
    public ResponseEntity<?> getAllArticleByTheme(@RequestParam("theme") String articleTheme) {
        List<ArticleDTO> articleDTOList = articleService.getAllArticleByArticleTheme(ArticleThemes.valueOf(articleTheme));
        return ResponseEntity.ok(articleDTOList);
    }

    @GetMapping("/get-All-article-by-region")
    public ResponseEntity<?> getAllArticleByRegion (@RequestParam("regionId") int regionId) {
        List<ArticleDTO> articleDTOList = articleService.getAllArtilceByRegion(regionId);
        return ResponseEntity.ok(articleDTOList);
    }
    @GetMapping("/get-All-article-by-createdDate")
    public ResponseEntity<?> getAllArticleByCreatedDate (@RequestParam("createdDate") String createdDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(createdDate, formatter);
        List<ArticleDTO> articleDTOList = articleService.getAllArticleByCreatedDate(localDate);
        return ResponseEntity.ok(articleDTOList);
    }
    @PutMapping("/publish-by-title")
    public ResponseEntity<?> publishByTile(HttpServletRequest request, @Valid @RequestBody ArticleDTO articleDTO) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request, ProfileRole.PUBLISHER_ROLE);
        List<ArticleDTO> articleDTOList = articleService.publish(articleDTO.getTitle(), profileJwtDTO.getId());
        return ResponseEntity.ok(articleDTOList);
    }
    @PutMapping("/block-by-title")
    public ResponseEntity<?> blockByTitle (HttpServletRequest request, @Valid @RequestBody ArticleDTO articleDTO) {
        JwtUtil.getProfile(request, ProfileRole.PUBLISHER_ROLE);
        List<ArticleDTO> articleDTOList = articleService.blocked(articleDTO.getTitle());
        return ResponseEntity.ok(articleDTOList);
    }
    @PostMapping("/filter-spec")
    public ResponseEntity<?> filterSpecification(@RequestBody ArticleFilterDTO articleFilterDTO) {
        PageImpl<ArticleDTO> articleDTOPage = articleService.filterSpe(0, 3, articleFilterDTO);
        return ResponseEntity.ok(articleDTOPage);
    }


}
