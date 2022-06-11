package com.fobgochod.auth;

import com.fobgochod.auth.domain.FghAuthenticationToken;
import com.fobgochod.auth.holder.UserDetails;
import com.fobgochod.constant.FghConstants;
import com.fobgochod.exception.FghException;
import com.fobgochod.service.login.token.UserTokenService;
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
 * FghAuthenticationFilter.java
 *
 * @author Xiao
 * @date 2022/2/23 23:43
 */
public class FghAuthenticationFilter extends OncePerRequestFilter {

    private final UserTokenService userTokenService;

    public FghAuthenticationFilter(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            LocaleContextHolder.setLocale(I18nUtils.getLocale(request));
            String userToken = request.getHeader(FghConstants.HTTP_HEADER_USER_TOKEN);
            if (userToken != null) {
                UserDetails userDetails = userTokenService.getData(userToken);
                request.setAttribute(FghConstants.HTTP_HEADER_USER_INFO, userDetails);
                SecurityContextHolder.getContext().setAuthentication(new FghAuthenticationToken(userDetails));
            }
            chain.doFilter(request, response);
        } catch (FghException e) {
            ExceptionUtils.writeUnAuth(request, response, e);
        }
    }
}
