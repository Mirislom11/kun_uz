package com.company.aop;

import com.company.dto.LikeDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.LikeStatus;
import com.company.enums.ProfileStatus;
import com.company.exception.BadRequestException;
import com.company.exception.ForbiddenException;
import com.company.service.ProfileService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.JoinColumn;

@Aspect
@Component
public class LikeAspect {
    @Autowired
    private ProfileService profileService;

    @Before("execution(* *Like(..))")
    public void beforeCreateLike(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        for (Object object : objects) {
            if (object instanceof LikeDTO) {
                checkLikeStatus(object);
            }else if (object instanceof Integer) {
                checkProfile(object);
            }
        }
    }
    private void checkProfile(Object object) {
        ProfileEntity profileEntity = profileService.get((Integer) object);
        if (!profileEntity.getStatus().equals(ProfileStatus.REGISTRATION)) {
            throw new ForbiddenException("This profile not registration or block");
        }
    }
    private void checkLikeStatus(Object object) {
        LikeStatus likeStatus = ((LikeDTO) object).getLikeStatus();
        if (likeStatus == null) {
            throw new BadRequestException("Like status not found");
        }
    }


}
