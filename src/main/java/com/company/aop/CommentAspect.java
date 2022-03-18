package com.company.aop;

import com.company.dto.comment.CommentDTO;
import com.company.entity.CommentEntity;
import com.company.entity.ProfileEntity;
import com.company.exception.BadRequestException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.CommentRepository;
import com.company.service.ProfileService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class CommentAspect {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private CommentRepository commentRepository;

    @Before("execution(* createComment(..))")
    public void checkBeforeCreate(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        for (Object object : objects) {
            if (object instanceof CommentDTO) {
                String content  = ((CommentDTO) object).getContent();
                if (content == null || content.isEmpty()) {
                    throw new ItemNotFoundException("Content is null or  empty");
                }
            }else if (object instanceof Integer) {
                ProfileEntity profile = profileService.get((Integer) object);
                if (profile == null) {
                    throw new ItemNotFoundException("Profile is null");
                }
            }
        }
    }

    @Before("execution (* getMyCommentList(..))")
    public void checkBeforeGetMyCommentList(JoinPoint joinPoint){
        Object[] objects = joinPoint.getArgs();
        for (Object object : objects) {
            if (object instanceof Integer) {
                ProfileEntity profile = profileService.get((Integer) object);
                if (profile == null) {
                    throw new ItemNotFoundException("Profile is null");
                }
            }
        }
    }
    @Before("execution (* *MyComment(..))")
    public void checkBeforeUpdate(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        CommentEntity commentEntity = null;
        for (Object object : objects) {
            if (object instanceof Long) {
                 Optional<CommentEntity> optionalComment =  commentRepository.findById(Math.toIntExact((Long) object));
                 if (optionalComment.isPresent()) {
                     commentEntity = optionalComment.get();
                 }
            }else if (object instanceof Integer) {
                if (commentEntity!= null && !commentEntity.getProfileEntity().getId().equals((Integer) object)) {
                    throw new BadRequestException("This User not owner");
                }
            }
        }
    }

}
