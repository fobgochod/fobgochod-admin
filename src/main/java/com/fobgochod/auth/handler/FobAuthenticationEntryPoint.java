package com.fobgochod.auth.handler;

import com.fobgochod.domain.CommonErrorCode;
import com.fobgochod.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Component
public class FobAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Value("${spring.application.name}")
    private String sourceId;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) {
        ExceptionUtils.writeUnAuth(request, response, sourceId, CommonErrorCode.USER_TOKEN_INVALID);
    }
}
