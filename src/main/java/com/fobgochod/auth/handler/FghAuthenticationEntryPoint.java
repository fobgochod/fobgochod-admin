package com.fobgochod.auth.handler;

import com.fobgochod.domain.base.I18nCode;
import com.fobgochod.util.ExceptionUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Component
public class FghAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) {
        ExceptionUtils.writeUnAuth(request, response, I18nCode.USER_TOKEN_INVALID);
    }
}
