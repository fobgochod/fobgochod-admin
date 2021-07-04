package com.fobgochod.config;

import com.fobgochod.auth.FobAuthenticationFilter;
import com.fobgochod.auth.handler.AuthService;
import com.fobgochod.auth.handler.FghAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author chenxsa
 * @date: 2018-6-6 19:26
 * @description:权限拦截器
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String[] WHITE_LIST = {
            "/",
            "/favicon.ico",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js",
            "/actuator/**",
            "/**/share/**",
            "/**/preview/**",
            "/**/download/**",
            "/**/toAnyOne/**",
            "/env/**",
            "/auth/**",
            "/buckets/task",
            "/buckets/apply",
            "/user/password/**",
    };
    @Autowired
    private AuthService authService;
    @Autowired
    private FghAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(WHITE_LIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new FobAuthenticationFilter(authService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        http.headers().cacheControl();
    }
}
