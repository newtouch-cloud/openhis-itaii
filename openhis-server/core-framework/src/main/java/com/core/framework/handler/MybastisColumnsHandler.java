package com.core.framework.handler;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.core.common.core.domain.model.LoginUser;
import com.core.common.utils.SecurityUtils;

@Component
public class MybastisColumnsHandler implements MetaObjectHandler {

    // 设置数据新增时候的，字段自动赋值规则
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        String username = "system";
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (loginUser != null) {
                username = loginUser.getUsername();
            }
        } catch (Exception ignored) {
        }
        this.strictInsertFill(metaObject, "createBy", String.class, username);
        this.strictInsertFill(metaObject, "tenantId", Integer.class, getCurrentTenantId());
    }

    // 设置数据修改update时候的，字段自动赋值规则
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        String username = "system";
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (loginUser != null) {
                username = loginUser.getUsername();
            }
        } catch (Exception ignored) {
        }
        this.strictUpdateFill(metaObject, "updateBy", String.class, username);
    }

    /**
     * 获取当前租户 ID
     */
    private Integer getCurrentTenantId() {
        // 获取当前登录用户的租户 ID
        if (SecurityUtils.getAuthentication() != null) {
            return SecurityUtils.getLoginUser().getTenantId();
        }
        return 0;
    }
}
