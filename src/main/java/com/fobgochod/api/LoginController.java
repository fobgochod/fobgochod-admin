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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUser body) {
        for (LoginService loginService : loginServices) {
            if (loginService.support(body)) {
                loginService.login(body);
                break;
            }
        }
        if (!StringUtils.hasText(body.getToken())) {
            throw new UnauthorizedException(I18nCode.LOGIN_AUTH_FAIL);
        }
        return ResponseEntity.ok(body);
    }

    @PostMapping(value = "/login/captcha")
    public ResponseEntity<?> captcha(@RequestBody LoginUser body) {
        aliyunSmsService.captcha(body.getTelephone());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/token/analyze")
    public ResponseEntity<?> analyze(@RequestAttribute(FghConstants.HTTP_HEADER_USER_INFO) LoginUser body) {
        return ResponseEntity.ok(body);
    }

    @PostMapping(value = "/token/refresh")
    public ResponseEntity<?> refresh(@RequestHeader(FghConstants.HTTP_HEADER_USER_TOKEN) String token,
                                     @RequestBody LoginUser body) {
        for (LoginService loginService : loginServices) {
            if (loginService.support(body)) {
                return ResponseEntity.ok(loginService.refresh(token, body.getTenantId()));
            }
        }
        return ResponseEntity.ok(body);
    }
}
