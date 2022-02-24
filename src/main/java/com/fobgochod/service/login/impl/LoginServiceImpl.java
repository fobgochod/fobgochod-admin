package com.fobgochod.service.login.impl;

import com.fobgochod.auth.domain.LoginType;
import com.fobgochod.auth.domain.LoginUser;
import com.fobgochod.entity.admin.User;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.login.LoginService;
import com.fobgochod.service.login.token.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * DMC 管理员账号登录
 *
 * @author zhouxiao
 * @date 2021/3/8
 */
@Order(1)
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenService userTokenService;

    @Override
    public boolean support(LoginUser loginUser) {
        return loginUser.getLoginType() == null || loginUser.getLoginType() == LoginType.token;
    }

    @Override
    public boolean validate(String token) {
        return userTokenService.validate(token);
    }

    @Override
    public String login(LoginUser loginUser) {
        User user = userRepository.findByCodeAndPassword(loginUser.getUsername(), loginUser.getPassword());
        if (user == null) {
            return null;
        }
        return userTokenService.getToken(loginUser);
    }

    @Override
    public String refresh(String token, String tenantId) {
        LoginUser jwtUser = userTokenService.getData(token);
        jwtUser.setTenantId(tenantId);
        return userTokenService.getToken(jwtUser);
    }

    @Override
    public LoginUser analysis(String token) {
        return userTokenService.getData(token);
    }
}
