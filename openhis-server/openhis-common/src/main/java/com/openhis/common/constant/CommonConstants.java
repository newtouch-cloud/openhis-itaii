package com.openhis.common.constant;

/**
 * 常量
 * 
 * @author system
 */
public class CommonConstants {

    /**
     * 共同常量
     */
    public interface Common {
        /**
         * 租户ID字段名称
         */
        String TENANT_ID = "tenant_id";

        /**
         * 开始时间（用于分页条件查询）
         */
        String S_TIME = "STime";

        /**
         * 结束时间（用于分页条件查询）
         */
        String E_TIME = "ETime";
    }

    /**
     * 表名常量
     */
    public interface TableName {

        /**
         * 服务管理
         */
        String ADM_HEALTHCARE_SERVICE = "adm_healthcare_service";
    }

    /**
     * 字段名常量
     */
    public interface FieldName {

        /**
         * 业务编码
         */
        String BusNo = "bus_no";
    }

}
