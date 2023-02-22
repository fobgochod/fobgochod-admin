package com.fobgochod.api;

import com.fobgochod.auth.domain.LoginUser;
import com.fobgochod.auth.holder.UserDetails;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.exception.UnauthorizedException;
import com.fobgochod.service.login.LoginService;
import com.fobgochod.service.message.sms.AliyunSmsService;
import com.fobgochod.util.SecureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        String password = SecureUtils.aesDecrypt(body.getPassword(), SecureUtils.AES_SECRET_KEY);
        body.setPassword(SecureUtils.sha256(password));
        for (LoginService loginService : loginServices) {
            if (loginService.support(body)) {
                UserDetails userDetails = loginService.login(body);
                return ResponseEntity.ok(userDetails);
            }
        }
        throw new UnauthorizedException(I18nCode.LOGIN_CAPTCHA_FAIL);
    }

    @PostMapping(value = "/login/captcha")
    public ResponseEntity<?> captcha(@RequestBody LoginUser body) {
        aliyunSmsService.captcha(body.getTelephone());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/token/analyze")
    public ResponseEntity<?> analyze(@RequestAttribute(FghConstants.HTTP_HEADER_USER_INFO) UserDetails userDetails) {
        return ResponseEntity.ok(userDetails);
    }

    @PostMapping(value = "/token/refresh")
    public ResponseEntity<?> refresh(@RequestHeader(FghConstants.HTTP_HEADER_USER_TOKEN) String token, @RequestBody LoginUser body) {
        for (LoginService loginService : loginServices) {
            if (loginService.support(body)) {
                return ResponseEntity.ok(loginService.refresh(token, body.getTenantCode()));
            }
        }
        return ResponseEntity.ok(body);
    }

    @PostMapping(value = "/login/security")
    public ResponseEntity<?> publicKey(@RequestBody Map<String, String> body) {
        String publicKey = body.get("publicKey");
        String encryptSecretKey = SecureUtils.rsaEncrypt(SecureUtils.AES_SECRET_KEY, publicKey);

        Map<String, String> map = Collections.singletonMap("secretKey", encryptSecretKey);
        return ResponseEntity.ok(map);
    }
}
