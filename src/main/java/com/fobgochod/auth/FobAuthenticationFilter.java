package com.fobgochod.auth;

import com.fobgochod.auth.handler.AuthService;
import com.fobgochod.domain.v2.FileOpTreeContextHolder;
import com.fobgochod.domain.CommonCode;
import com.fobgochod.domain.CommonErrorCode;
import com.fobgochod.exception.FghException;
import com.fobgochod.util.ExceptionUtils;
import com.fobgochod.util.I18nUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenxsa
 * @date: 2018-6-7 01:43
 * @Description:
 */

public class FobAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public FobAuthenticationFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            LocaleContextHolder.setLocale(I18nUtils.getLocale(request));
            authService.getAuth(request);
            chain.doFilter(request, response);
        } catch (FghException e) {
            ExceptionUtils.writeUnAuth(request, response, CommonCode.DMC.name(), e);
        } catch (Exception e) {
            ExceptionUtils.writeUnAuth(request, response, CommonCode.DMC.name(), CommonErrorCode.USER_TOKEN_INVALID);
        } finally {
            SecurityContextHolder.clearContext();
            FileOpTreeContextHolder.resetContext();
        }
    }
}
