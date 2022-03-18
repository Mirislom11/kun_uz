package com.company.controller;

import com.company.dto.LikeDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.enums.LikeStatus;
import com.company.service.ArticleLikeService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private ArticleLikeService articleLikeService;

    @PostMapping("/create")
    public ResponseEntity<?> create (@Valid @RequestBody LikeDTO likeDTO, HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request);
        LikeDTO response = articleLikeService.createLike(likeDTO, profileJwtDTO.getId());
        return ResponseEntity.ok(response);
    }
    @GetMapping("get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id, HttpServletRequest request) {
        JwtUtil.getProfile(request);
        LikeDTO response = articleLikeService.getLikeById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-count-by-article/{id}")
    public ResponseEntity<?> getCountOfLikeToArticle(@PathVariable("id") int id, HttpServletRequest request) {
        JwtUtil.getProfile(request);
        long count = articleLikeService.getCountOfLikeByArticle(id);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/delete-like/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id, HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request);
        String response = articleLikeService.deleteLikeById(id, profileJwtDTO.getId());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-my-likes/{status}")
    public ResponseEntity<?> getMyLikes(@PathVariable("status") String status, HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request);
        List<LikeDTO> likeDTOList =  articleLikeService.getAllMyLikeOrDislike(profileJwtDTO.getId(), LikeStatus.valueOf(status));
        return ResponseEntity.ok(likeDTOList);
    }
    @PutMapping("/update-like/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, HttpServletRequest request,
                                    @Valid @RequestBody LikeDTO likeDTO){
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request);
        String response = articleLikeService.updateLike(likeDTO, profileJwtDTO.getId(), id);
        return ResponseEntity.ok(response);
    }



}
