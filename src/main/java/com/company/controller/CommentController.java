package com.company.controller;

import com.company.dto.comment.CommentDTO;
import com.company.dto.ProfileJwtDTO;
import com.company.dto.comment.CommentFilterDTO;
import com.company.service.CommentService;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(commentService.createComment(commentDTO, profileJwtDTO.getId()));
    }

    @GetMapping("/get-ALL")
    public ResponseEntity<?> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }
    @GetMapping("/get-my-comments")
    public ResponseEntity<?> getMyComments(HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(commentService.getMyCommentList(profileJwtDTO.getId()));
    }

    @GetMapping("/get-comments-by-articleId/{id}")
    public ResponseEntity<?> getCommentsByArticleId (@PathVariable("id")Integer id) {
        return ResponseEntity.ok(commentService.getAllCommentsByArticleId(id));
    }
    @DeleteMapping("/delete-comments")
    public ResponseEntity<?> deleteCommentById(@RequestParam("commentId") Integer commentId, HttpServletRequest request) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(commentService.deleteMyComment(commentId, profileJwtDTO.getId()));
    }
    @PutMapping("/update-comment")
    public ResponseEntity<?> updateComment(@RequestParam("commentId") Integer id, HttpServletRequest request,
                                           @Valid @RequestBody CommentDTO commentDTO) {
        ProfileJwtDTO profileJwtDTO = JwtUtil.getProfile(request);
        return ResponseEntity.ok(commentService.updateMyComment(id, profileJwtDTO.getId(), commentDTO));
    }
    @PostMapping("/filter-spec")
    public ResponseEntity<?> filterSpec(@RequestBody CommentFilterDTO filterDTO, @RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(commentService.filterSpec(page, size, filterDTO));
    }

}
