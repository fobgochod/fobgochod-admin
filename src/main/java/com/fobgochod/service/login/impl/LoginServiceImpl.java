package com.fobgochod.service.login.impl;

import com.fobgochod.auth.domain.LoginType;
import com.fobgochod.auth.domain.LoginUser;
import com.fobgochod.auth.holder.UserDetails;
import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.entity.admin.Tenant;
import com.fobgochod.entity.admin.User;
import com.fobgochod.exception.UnauthorizedException;
import com.fobgochod.repository.TenantRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.login.LoginService;
import com.fobgochod.service.login.token.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    private TenantRepository tenantRepository;
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
    public UserDetails login(LoginUser loginUser) {
        User user = userRepository.findByCode(loginUser.getUsername());
        if (user == null) {
            throw new UnauthorizedException(I18nCode.LOGIN_ACCOUNT_FAIL);
        }
        if (!Objects.equals(user.getPassword(), loginUser.getPassword())) {
            throw new UnauthorizedException(I18nCode.LOGIN_ACCOUNT_FAIL);
        }
        Tenant tenant = tenantRepository.findByCode(user.getTenantCode());
        UserDetails userDetails = UserDetails.of(user, tenant);
        userDetails.setToken(userTokenService.getToken(userDetails));
        return userDetails;
    }

    @Override
    public UserDetails refresh(String token, String tenantCode) {
        UserDetails userDetails = userTokenService.getData(token);
        userDetails.of(tenantRepository.findByCode(tenantCode));
        userDetails.setToken(null);
        userDetails.setToken(userTokenService.getToken(userDetails));
        return userDetails;
    }

    @Override
    public UserDetails analysis(String token) {
        return userTokenService.getData(token);
    }
}
