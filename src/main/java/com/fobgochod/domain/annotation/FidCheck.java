package com.fobgochod.domain.annotation;

import com.fobgochod.domain.enumeration.FidType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解用于标记需要校验的文件ID参数
 *
 * @author zhouxiao
 * @date 2021/3/11
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FidCheck {

    /**
     * 类型 、文件、目录、分享、回收站
     *
     * @return
     */
    FidType value() default FidType.File;
}
