package com.fobgochod.auth;

import com.fobgochod.auth.domain.LoginUser;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.domain.FileOpTreeContextHolder;
import com.fobgochod.exception.FghException;
import com.fobgochod.service.login.LoginService;
import com.fobgochod.util.ExceptionUtils;
import com.fobgochod.util.I18nUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * FghAuthenticationFilter.java
 *
 * @author Xiao
 * @date 2022/2/23 23:43
 */
public class FghAuthenticationFilter extends OncePerRequestFilter {

    private final LoginService loginService;

    public FghAuthenticationFilter(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            LocaleContextHolder.setLocale(I18nUtils.getLocale(request));
            String userToken = request.getHeader(FghConstants.HTTP_HEADER_USER_TOKEN);
            if (userToken != null) {
                LoginUser loginUser = loginService.analysis(userToken);
                request.setAttribute(FghConstants.HTTP_HEADER_USER_INFO, loginUser);
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, Collections.emptyList());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (FghException e) {
            ExceptionUtils.writeUnAuth(request, response, e);
        } finally {
            SecurityContextHolder.clearContext();
            FileOpTreeContextHolder.resetContext();
        }
    }
}
