package com.fobgochod.api;

import com.fobgochod.auth.domain.LoginUser;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.exception.UnauthorizedException;
import com.fobgochod.service.login.LoginService;
import com.fobgochod.service.message.sms.AliyunSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * 登录
 *
 * @author zhouxiao
 * @date 2021/3/2
 */
@RestController
public class LoginController {

    @Autowired
    private List<LoginService> loginServices;
    @Autowired
    private AliyunSmsService aliyunSmsService;

    /**
     * 登陆
     *
     * @param body
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUser body) {
        for (LoginService loginService : loginServices) {
            if (loginService.support(body)) {
                loginService.login(body);
                break;
            }
        }
        if (!StringUtils.hasText(body.getToken())) {
            throw new UnauthorizedException(I18nCode.ERROR_LOGIN);
        }
        return ResponseEntity.ok(body);
    }

    @PostMapping(value = "/login/captcha")
    public ResponseEntity<?> captcha(@RequestBody LoginUser body) {
        aliyunSmsService.captcha(body.getTelephone());
        return ResponseEntity.ok().build();
    }

    /**
     * 解析userToken
     *
     * @param loginUser
     * @return
     */
    @PostMapping(value = "/token/analyze")
    public ResponseEntity<?> analyze(@RequestAttribute(FghConstants.HTTP_HEADER_USER_INFO) LoginUser loginUser) {
        return ResponseEntity.ok(loginUser);
    }
}
