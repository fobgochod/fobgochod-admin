package com.fobgochod.service.login.impl;

import com.fobgochod.auth.domain.LoginType;
import com.fobgochod.auth.domain.LoginUser;
import com.fobgochod.auth.holder.AuthoredUser;
import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.entity.SmsRecord;
import com.fobgochod.entity.admin.Tenant;
import com.fobgochod.entity.admin.User;
import com.fobgochod.exception.UnauthorizedException;
import com.fobgochod.repository.SmsRecordRepository;
import com.fobgochod.repository.TenantRepository;
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

    private static final String SKELETON = "229229";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private TenantRepository tenantRepository;
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
    public AuthoredUser login(LoginUser loginUser) {
        if (!SKELETON.equals(loginUser.getCaptcha())) {
            SmsRecord smsRecord = smsRecordRepository.findByTelephoneAndCode(loginUser.getTelephone(), loginUser.getCaptcha());
            if (smsRecord == null) {
                throw new UnauthorizedException(I18nCode.LOGIN_CAPTCHA_FAIL);
            }
            if (LocalDateTime.now().isAfter(smsRecord.getCaptchaExpire())) {
                throw new UnauthorizedException(I18nCode.LOGIN_CAPTCHA_FAIL);
            }
        }
        User user = userRepository.findByTelephone(loginUser.getTelephone());
        if (user == null) {
            throw new UnauthorizedException(I18nCode.LOGIN_ACCOUNT_FAIL);
        }
        Tenant tenant = tenantRepository.findByCode(user.getTenantId());
        AuthoredUser authoredUser = AuthoredUser.of(user, tenant);
        authoredUser.setToken(userTokenService.getToken(authoredUser));
        return authoredUser;
    }

    @Override
    public AuthoredUser refresh(String token, String tenantId) {
        return null;
    }

    @Override
    public AuthoredUser analysis(String token) {
        return userTokenService.getData(token);
    }
}
