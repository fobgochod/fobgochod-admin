package com.fobgochod.auth.holder;

/**
 * 登陆用户信息
 *
 * @author seven
 * @date 2020/6/7
 */
public final class AppAuthContextHolder {

    private static final ThreadLocal<AuthoredUser> contextHolder = new ThreadLocal<>();

    AppAuthContextHolder() {
    }

    public static void clearContext() {
        contextHolder.remove();
    }

    public static AuthoredUser getContext() {
        AuthoredUser ctx = contextHolder.get();
        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }
        return ctx;
    }

    public static void setContext(AuthoredUser context) {
        contextHolder.set(context);
    }

    public static AuthoredUser createEmptyContext() {
        return  AuthoredUser.of();
    }
}
