package com.fobgochod.util;

import com.fobgochod.auth.holder.AppAuthContextHolder;
import com.fobgochod.auth.holder.AuthoredUser;
import com.fobgochod.entity.BaseEntity;

import java.time.LocalDateTime;

/**
 * 当前线程变成信息
 *
 * @author zhouxiao
 * @date 2020/6/11
 */
public class TokenUtils {

    /**
     * 获取当前登录用户
     *
     * @return AuthoredUser
     */
    public static AuthoredUser getAuthoredUser() {
        return AppAuthContextHolder.getContext();
    }

    /**
     * 获取当前登录用户token
     *
     * @return userToken
     */
    public static String getToken() {
        AuthoredUser authoredUser = TokenUtils.getAuthoredUser();
        if (authoredUser != null) {
            return authoredUser.getToken();
        }
        return null;
    }

    /**
     * 获取当前用户sid
     *
     * @return userSid
     */
    public static long getUserSid() {
        AuthoredUser authoredUser = TokenUtils.getAuthoredUser();
        if (authoredUser != null) {
            return authoredUser.getSid();
        }
        return 0;
    }

    /**
     * 获取当前用户id
     *
     * @return userId
     */
    public static String getUserId() {
        AuthoredUser authoredUser = TokenUtils.getAuthoredUser();
        if (authoredUser != null) {
            return authoredUser.getUserId();
        }
        return null;
    }

    /**
     * 获取当前用户name
     *
     * @return userName
     */
    public static String getUserName() {
        AuthoredUser authoredUser = TokenUtils.getAuthoredUser();
        if (authoredUser != null) {
            return authoredUser.getUserName();
        }
        return null;
    }

    /**
     * 新增数据，给基础字段赋值
     *
     * @param entity entity
     */
    public static void setCreateFields(BaseEntity entity) {
        //设置创建字段值
        entity.setCreateBy(TokenUtils.getUserSid());
        entity.setCreateById(TokenUtils.getUserName());
        entity.setCreateDate(LocalDateTime.now());
        //设置修改字段值
        entity.setModifyBy(TokenUtils.getUserSid());
        entity.setModifyById(TokenUtils.getUserName());
        entity.setModifyDate(LocalDateTime.now());
    }


    /**
     * 修改数据，给基础字段赋值
     *
     * @param entity entity
     */
    public static void setModifyFields(BaseEntity entity) {
        //设置修改字段值
        entity.setModifyBy(TokenUtils.getUserSid());
        entity.setModifyById(TokenUtils.getUserName());
        entity.setModifyDate(LocalDateTime.now());
    }
}
