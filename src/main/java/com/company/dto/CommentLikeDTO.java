package com.company.dto;

import com.company.enums.LikeStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentLikeDTO {
    private int id;
    private LikeStatus likeStatus;
    private int profileId;
    private LocalDateTime createdDateTime;
    private int commentId;
}
