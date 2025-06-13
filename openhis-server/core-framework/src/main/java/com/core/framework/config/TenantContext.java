package com.core.framework.config;

/**
 * TenantContext类 来管理线程局部变量中的租户ID
 */
public class TenantContext {
    private static final ThreadLocal<Integer> CURRENT_TENANT = new ThreadLocal<>();

    public static void setCurrentTenant(Integer tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static Integer getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
