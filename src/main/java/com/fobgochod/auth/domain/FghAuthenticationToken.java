package com.fobgochod.auth.domain;

import com.fobgochod.auth.holder.UserDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import jakarta.validation.constraints.NotNull;

/**
 * FghAuthenticationToken.java
 *
 * @author Xiao
 * @date 2022/6/4 19:55
 */
public class FghAuthenticationToken extends AbstractAuthenticationToken {

    private final UserDetails user;

    public FghAuthenticationToken(@NotNull UserDetails user) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.user = user;
        super.setDetails(user);
        super.setAuthenticated(true);
    }

    @Override
    public Object getDetails() {
        return this.user;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.user;
    }
}
