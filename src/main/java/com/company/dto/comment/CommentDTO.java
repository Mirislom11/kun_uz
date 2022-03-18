package com.company.dto.comment;

import com.company.entity.ArticleEntity;
import com.company.entity.ProfileEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class CommentDTO {
    private long id;
    @NotEmpty(message = "content of comment null or empty")
    private String content;
    @Positive(message = "article id must by positive")
    @NotEmpty(message = "article id null or empty")
    private int articleId;
    @Positive(message = "profile id must by positive")
    @NotEmpty(message = "profile id null or empty")
    private int profileId;
    private LocalDateTime createdDateTime;
    private String articleName;
    private String profileName;
}
