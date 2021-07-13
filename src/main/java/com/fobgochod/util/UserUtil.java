package com.fobgochod.util;

import com.fobgochod.auth.domain.JwtUser;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.entity.BaseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

/**
 * 当前登录用户
 *
 * @author zhouxiao
 * @date 2020/5/13
 */
public class UserUtil {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static JwtUser getJwtUser() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof JwtUser) {
                return (JwtUser) principal;
            }
        }
        return new JwtUser(FghConstants.ANONYMOUS_USER, FghConstants.ANONYMOUS_USER);
    }

    public static long getUserSid() {
        return 0L;
    }

    public static String getUserId() {
        return getJwtUser().getId();
    }

    public static String getUserName() {
        return getJwtUser().getUsername();
    }

    public static String getTenantId() {
        return getJwtUser().getTenantId();
    }

    public static void setCreateFields(BaseEntity entity) {
        entity.setCreateBy(getUserSid());
        entity.setCreateById(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        entity.setModifyBy(getUserSid());
        entity.setModifyById(getUserName());
        entity.setModifyDate(LocalDateTime.now());
    }

    public static void setModifyFields(BaseEntity entity) {
        entity.setModifyBy(getUserSid());
        entity.setModifyById(getUserName());
        entity.setModifyDate(LocalDateTime.now());
    }

    public static void setModifyFields(BaseEntity data, BaseEntity old) {
        if (old != null) {
            data.setCreateBy(old.getCreateBy());
            data.setCreateById(old.getCreateById());
            data.setCreateDate(old.getCreateDate());
        }
        setModifyFields(data);
    }

    public static void copyCreate(BaseEntity source, BaseEntity target) {
        source.setCreateBy(target.getCreateBy());
        source.setCreateById(target.getCreateById());
        source.setCreateDate(target.getCreateDate());
        source.setModifyBy(target.getModifyBy());
        source.setModifyById(target.getModifyById());
        source.setModifyDate(target.getModifyDate());
    }
}
