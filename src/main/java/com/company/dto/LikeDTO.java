package com.company.dto;

import com.company.enums.LikeStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.implementation.bind.annotation.Empty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO {
    private long id;
    @NotEmpty(message = "like status null or empty")
    @NotBlank(message = "like status has not be space")
    private LikeStatus likeStatus;
    @Positive
    @NotEmpty(message = "profile id null or empty")
    private int profileId;
    private LocalDateTime createdDateTime;
    private int  articleId;
}
