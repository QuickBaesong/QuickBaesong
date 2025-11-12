package com.qb.common.security;

/** ThreadLocal 에 인증 정보 저장 (SecurityContextHolder 대신) */
public class UserContextHolder {
    private static final ThreadLocal<UserContext> userContext = new ThreadLocal<>();

    public static UserContext get() {
        return userContext.get();
    }

    public static void set(UserContext context) {
        userContext.set(context);
    }

    public static void clear() {
        userContext.remove();
    }
}
