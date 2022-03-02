package com.fobgochod.service.login.impl;

import com.fobgochod.auth.domain.LoginType;
import com.fobgochod.auth.domain.LoginUser;
import com.fobgochod.entity.admin.SmsRecord;
import com.fobgochod.entity.admin.User;
import com.fobgochod.exception.SystemException;
import com.fobgochod.repository.SmsRecordRepository;
import com.fobgochod.repository.UserRepository;
import com.fobgochod.service.login.LoginService;
import com.fobgochod.service.login.token.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * DMC 管理员账号登录
 *
 * @author zhouxiao
 * @date 2021/3/8
 */
@Order(2)
@Service("smsLoginService")
public class SmsLoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private SmsRecordRepository smsRecordRepository;

    @Override
    public boolean support(LoginUser loginUser) {
        return loginUser.getLoginType() == LoginType.captcha;
    }

    @Override
    public boolean validate(String token) {
        return true;
    }

    @Override
    public void login(LoginUser loginUser) {
        SmsRecord smsRecord = smsRecordRepository.findByTelephoneAndCode(loginUser.getTelephone(), loginUser.getCaptcha());
        if (smsRecord == null) {
            return;
        }
        if (LocalDateTime.now().isAfter(smsRecord.getCaptchaExpire())) {
            throw new SystemException("验证码已经过期");
        }
        User user = userRepository.findByTelephone(loginUser.getTelephone());
        loginUser.setUsername(user.getCode());
        loginUser.setToken(userTokenService.getToken(loginUser));
    }

    @Override
    public String refresh(String token, String tenantId) {
        return null;
    }

    @Override
    public LoginUser analysis(String token) {
        return userTokenService.getData(token);
    }
}
