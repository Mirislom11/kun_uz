package com.company.controller;

import com.company.dto.CommentLikeDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.enums.LikeStatus;
import com.company.service.CommentLikeService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/com-like/")
public class CommentLikeController {
    @Autowired
    private CommentLikeService commentLikeService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CommentLikeDTO commentLikeDTO, HttpServletRequest request){
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request);
        CommentLikeDTO response = commentLikeService.createLikeComment(commentLikeDTO, profileJwtDTO.getId());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id, HttpServletRequest request) {
        JwtUtil.getProfile(request);
        CommentLikeDTO response = commentLikeService.getCommentLikeById(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("get/ALL")
    public ResponseEntity<?> getAll () {
        List<CommentLikeDTO> commentLikeDTOList = commentLikeService.getListOfCommentLike();
        return ResponseEntity.ok(commentLikeDTOList);
    }
    @GetMapping("get-by-profileId/{likeStatus}")
    public ResponseEntity<?> getProfileLikeOrDislike(@PathVariable("likeStatus") String likeStatus,
                                                     HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(commentLikeService.getCommentLikDTOByProfileId(profileJwtDTO.getId(),
                LikeStatus.valueOf(likeStatus)));

    }
    @GetMapping("/get-by-commentId/{commentId}/{likeStatus}")
    public ResponseEntity<?> getCommentLikeListByCommentId(@PathVariable("commentId") int commentId,
                                                           @PathVariable("likeStatus") String likeStatus){
        return ResponseEntity.ok(commentLikeService.getCommentLikeDTOByCommentId(LikeStatus.valueOf(likeStatus), commentId));
    }
    @DeleteMapping("/delete-commentLike/{id}")
    public ResponseEntity<?> delete (@PathVariable("id") int id) {
        return ResponseEntity.ok(commentLikeService.deleteCommentLikeById(id));
    }
    @PutMapping("update-commentLike{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody CommentLikeDTO commentLikeDTO) {
        return ResponseEntity.ok(commentLikeService.updateCommentLikeById(id, commentLikeDTO));
    }
}
