package com.fobgochod.service.login;

import com.fobgochod.auth.domain.LoginUser;

/**
 * 统一登录接口
 *
 * @author zhouxiao
 * @date 2021/5/20
 */
public interface LoginService {

    /**
     * 是否支持
     *
     * @param loginUser
     * @return
     */
    boolean support(LoginUser loginUser);

    /**
     * 根据token换取请求
     *
     * @param token
     * @return
     */
    boolean validate(String token);

    /**
     * 登陆验证
     *
     * @param loginUser
     * @return 通过授权的token
     */
    String login(LoginUser loginUser);

    /**
     * 刷新token
     *
     * @param token
     * @param tenantId
     * @return
     */
    String refresh(String token, String tenantId);

    /**
     * 解析token
     *
     * @param token
     * @return
     */
    LoginUser analysis(String token);
}
