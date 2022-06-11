package com.fobgochod.util;

import com.fobgochod.auth.holder.UserDetails;
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

    public static UserDetails getUser() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return (UserDetails) principal;
            }
        }
        return UserDetails.of(FghConstants.ANONYMOUS_USER);
    }

    public static String getUserCode() {
        return getUser().getUserCode();
    }

    public static String getUserName() {
        return getUser().getUserName();
    }

    public static String getTenantCode() {
        return getUser().getTenantCode();
    }

    public static void setCreateFields(BaseEntity entity) {
        entity.setCreateCode(getUserCode());
        entity.setCreateName(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        entity.setModifyCode(getUserCode());
        entity.setModifyName(getUserName());
        entity.setModifyDate(LocalDateTime.now());
    }

    public static void setModifyFields(BaseEntity entity) {
        entity.setModifyCode(getUserCode());
        entity.setModifyName(getUserName());
        entity.setModifyDate(LocalDateTime.now());
    }

    public static void setModifyFields(BaseEntity data, BaseEntity old) {
        if (old != null) {
            data.setCreateCode(old.getCreateCode());
            data.setCreateName(old.getCreateName());
            data.setCreateDate(old.getCreateDate());
        }
        setModifyFields(data);
    }

    public static void copyCreate(BaseEntity source, BaseEntity target) {
        source.setCreateCode(target.getCreateCode());
        source.setCreateName(target.getCreateName());
        source.setCreateDate(target.getCreateDate());
        source.setModifyCode(target.getModifyCode());
        source.setModifyName(target.getModifyName());
        source.setModifyDate(target.getModifyDate());
    }
}
