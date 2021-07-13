package com.fobgochod.auth;

import com.fobgochod.auth.domain.JwtUser;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.FileOpTreeContextHolder;
import com.fobgochod.domain.base.CommonErrorCode;
import com.fobgochod.exception.BusinessException;
import com.fobgochod.exception.FghException;
import com.fobgochod.service.login.LoginService;
import com.fobgochod.util.ExceptionUtils;
import com.fobgochod.util.I18nUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author chenxsa
 * @date: 2018-6-7 01:43
 * @Description:
 */

public class FghAuthenticationFilter extends OncePerRequestFilter {

    private final LoginService loginService;

    public FghAuthenticationFilter(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            LocaleContextHolder.setLocale(I18nUtils.getLocale(request));
            String userToken = request.getHeader(FghConstants.HTTP_HEADER_USER_TOKEN_KEY);
            JwtUser jwtUser = this.analysisUserToken(userToken);
            if (jwtUser != null) {
                request.setAttribute(FghConstants.HTTP_HEADER_USER_INFO_KEY, jwtUser);
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, Collections.emptyList());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (FghException e) {
            ExceptionUtils.writeUnAuth(request, response, e);
        } catch (Exception e) {
            ExceptionUtils.writeUnAuth(request, response, CommonErrorCode.USER_TOKEN_INVALID);
        } finally {
            SecurityContextHolder.clearContext();
            FileOpTreeContextHolder.resetContext();
        }
    }

    private JwtUser analysisUserToken(String token) {
        if (StringUtils.hasText(token)) {
            try {
                return loginService.analysis(token);
            } catch (Exception e) {
                throw new BusinessException("userToken已经过期或者无效");
            }
        }
        return null;
    }
}
