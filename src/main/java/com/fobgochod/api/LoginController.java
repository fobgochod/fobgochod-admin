package com.fobgochod.api;

import com.fobgochod.auth.domain.LoginUser;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.exception.UnauthorizedException;
import com.fobgochod.service.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;

/**
 * 登录
 *
 * @author zhouxiao
 * @date 2021/3/2
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 登陆
     *
     * @param body
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUser body) {
        String userToken = loginService.login(body);
        if (!StringUtils.hasText(userToken)) {
            throw new UnauthorizedException(I18nCode.ERROR_LOGIN);
        }
        return ResponseEntity.ok(Collections.singletonMap("userToken", userToken));
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

    /**
     * 租户刷新token
     *
     * @param userToken
     * @param body
     * @return
     */
    @PostMapping(value = "/token/refresh")
    public ResponseEntity<?> refresh(@RequestHeader(FghConstants.HTTP_HEADER_USER_TOKEN) String userToken,
                                     @RequestBody LoginUser body) {
        String refresh = loginService.refresh(userToken, body.getTenantId());
        return ResponseEntity.ok(Collections.singletonMap("userToken", refresh));
    }
}
