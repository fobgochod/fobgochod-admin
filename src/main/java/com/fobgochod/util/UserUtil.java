package com.fobgochod.util;

import com.fobgochod.auth.domain.JwtUser;
import com.fobgochod.constant.DmcConstants;
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

    /**
     * 获取线程变量验证信息
     *
     * @return
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 设置线程变量验证信息
     *
     * @param authentication
     */
    public static void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 获取当前线程用户信息
     * 没有返回匿名用户anonymousUser
     *
     * @return JwtUser
     */
    public static JwtUser getJwtUser() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof JwtUser) {
                return (JwtUser) principal;
            }
        }
        // 匿名用户
        return new JwtUser(DmcConstants.ANONYMOUS_USER, DmcConstants.ANONYMOUS_USER);
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
