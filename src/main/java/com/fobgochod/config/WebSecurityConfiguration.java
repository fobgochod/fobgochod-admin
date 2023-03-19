package com.fobgochod.config;

import com.fobgochod.auth.FghAuthenticationFilter;
import com.fobgochod.auth.handler.FghAuthenticationEntryPoint;
import com.fobgochod.service.login.token.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration(proxyBeanMethods = false)
public class WebSecurityConfiguration {

    private static final String[] AUTH_WHITELIST = {
            "/", "/env", "/actuator", "/login/**", "/medicines/me/**", "/file/preview", "/admin/**",
    };

    private final SecurityProperties security;

    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private FghAuthenticationEntryPoint authenticationEntryPoint;

    public WebSecurityConfiguration(SecurityProperties security) {
        this.security = security;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(request -> request
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new FghAuthenticationFilter(userTokenService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and().build();
    }

    // Required to provide UserDetailsService for "remember functionality"
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername(security.getUser().getName())
                .password(passwordEncoder.encode(security.getUser().getPassword()))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
