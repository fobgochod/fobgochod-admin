package com.fobgochod.config;

import com.fobgochod.auth.FghAuthenticationFilter;
import com.fobgochod.auth.handler.FghAuthenticationEntryPoint;
import com.fobgochod.service.login.token.UserTokenService;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.UUID;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

@Configuration(proxyBeanMethods = false)
public class WebSecurityConfiguration {

    private static final String[] AUTH_WHITELIST = {
            "/", "/env", "/actuator", "/login/**", "/medicines/me", "/file/preview", "/admin/**",
    };

    private final AdminServerProperties adminServer;
    private final SecurityProperties security;


    @Autowired
    private UserTokenService userTokenService;
    @Autowired
    private FghAuthenticationEntryPoint authenticationEntryPoint;

    public WebSecurityConfiguration(AdminServerProperties adminServer, SecurityProperties security) {
        this.adminServer = adminServer;
        this.security = security;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.path("/"));

        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(adminServer.path("/assets/**")).permitAll()
                        .requestMatchers(adminServer.path("/actuator/info")).permitAll()
                        .requestMatchers(adminServer.path("/actuator/health")).permitAll()
                        .requestMatchers(adminServer.path("/login")).permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin.loginPage(adminServer.path("/login")).successHandler(successHandler).and())
                .logout(logout -> logout.logoutUrl(adminServer.path("/logout")))
                .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(new FghAuthenticationFilter(userTokenService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher(this.adminServer.path("/instances"), POST.toString()),
                                new AntPathRequestMatcher(this.adminServer.path("/instances/*"), DELETE.toString()),
                                new AntPathRequestMatcher(this.adminServer.path("/actuator/**")),
                                new AntPathRequestMatcher("/**")
                        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.addAllowedOriginPattern(CorsConfiguration.ALL);
        corsConfiguration.setAllowCredentials(false);
        source.registerCorsConfiguration("/**", corsConfiguration);
        http.cors().configurationSource(source);

        http.rememberMe((rememberMe) -> rememberMe.key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600));

        return http.build();

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
