package com.core.framework.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.core.common.utils.SecurityUtils;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

@EnableTransactionManagement(proxyTargetClass = true)
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor());
        // 乐观锁插件
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
        // 阻断插件
        interceptor.addInnerInterceptor(blockAttackInnerInterceptor());
        // 多租户插件
        interceptor.addInnerInterceptor(tenantLineInnerInterceptor());
        return interceptor;
    }

    /**
     * 分页插件，自动识别数据库类型 https://baomidou.com/guide/interceptor-pagination.html
     */
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // 设置数据库类型为mysql
        // paginationInnerInterceptor.setDbType(DbType.MYSQL);
        paginationInnerInterceptor.setDbType(DbType.POSTGRE_SQL);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInnerInterceptor.setMaxLimit(-1L);
        return paginationInnerInterceptor;
    }

    /**
     * 乐观锁插件 https://baomidou.com/guide/interceptor-optimistic-locker.html
     */
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    /**
     * 如果是对全表的删除或更新操作，就会终止该操作 https://baomidou.com/guide/interceptor-block-attack.html
     */
    public BlockAttackInnerInterceptor blockAttackInnerInterceptor() {
        return new BlockAttackInnerInterceptor();
    }

    /**
     * 多租户插件
     */
    public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
        tenantInterceptor.setTenantLineHandler(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                // 返回当前租户 ID
                return new LongValue(getCurrentTenantId());
            }

            @Override
            public String getTenantIdColumn() {
                // 返回租户 ID 的字段名
                return "tenant_id";
            }

            // 配置需要租户隔离的表名集合
            private static final Set<String> TENANT_TABLES = new HashSet<>(Arrays.asList("adm_account",
                "adm_charge_item", "adm_charge_item_def_detail", "adm_charge_item_definition", "adm_device",
                "adm_device_definition", "adm_encounter", "adm_encounter_diagnosis", "adm_encounter_location",
                "adm_encounter_participant", "adm_encounter_reason", "adm_healthcare_service", "adm_invoice",
                "adm_location", "adm_organization", "adm_organization_location", "adm_patient",
                "adm_patient_identifier", "adm_practitioner", "adm_practitioner_role", "adm_supplier", "cli_condition",
                "cli_condition_definition", "cli_diagnosis_belong_binding", "cli_procedure", "cli_procedure_performer",
                "doc_emr", "doc_emr_template", "doc_emr_detail", "doc_emr_dict", "fin_claim", "fin_claim_response",
                "fin_contract", "fin_payment_notice", "fin_payment_rec_detail", "fin_payment_reconciliation",
                "med_medication", "med_medication_definition", "med_medication_dispense", "med_medication_request",
                "wor_activity_definition", "wor_device_dispense", "wor_device_request", "wor_inventory_item",
                "wor_service_request", "wor_service_request_detail", "wor_supply_delivery", "wor_supply_request",
                "sys_operation_record"));

            @Override
            public boolean ignoreTable(String tableName) {
                // 忽略不需要租户隔离的表
                // return false; // 默认所有表都开启租户隔离
                // 如果表名不在集合中，则忽略租户隔离
                return !TENANT_TABLES.contains(tableName);
            }
        });
        return tenantInterceptor;
    }

    /**
     * 获取当前租户 ID
     */
    private Integer getCurrentTenantId() {

        // 首先尝试从线程局部变量中获取租户ID（适用于定时任务等场景）
        Integer threadLocalTenantId = TenantContext.getCurrentTenant();
        if (threadLocalTenantId != null) {
            return threadLocalTenantId;
        }

        // 获取当前登录用户的租户ID（优先使用SecurityUtils中储存的LoginUser的租户ID）
        try {
            if (SecurityUtils.getAuthentication() != null) {
                return SecurityUtils.getLoginUser().getTenantId();
            }
        } catch (Exception e) {
            return 1; // 默认租户ID
        }

        // 尝试从请求头中获取租户ID
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 从请求头获取租户ID，假设header名称为"X-Tenant-ID" ; 登录接口前端把租户id放到请求头里
            String tenantIdHeader = request.getHeader("X-Tenant-ID");
            String requestMethodName = request.getHeader("Request-Method-Name");
            // 登录
            if ("login".equals(requestMethodName)) {
                if (tenantIdHeader != null && !tenantIdHeader.isEmpty()) {
                    return Integer.parseInt(tenantIdHeader);
                }
            }
        }

        return 1; // 默认租户ID
    }
}
