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

        /**
         * 逗号（用于拼接）
         */
        String COMMA_FORMAT = "%s,%s";

        /**
         * 逗号
         */
        String COMMA = ",";

        /**
         * 3（用于字符串截位）
         */
        Integer THREE = 3;

        /**
         * 中杠
         */
        String DASH = "-";
    }

    /**
     * 表名常量
     */
    public interface TableName {

        /**
         * 服务管理
         */
        String ADM_HEALTHCARE_SERVICE = "adm_healthcare_service";

        /**
         * 药品定义
         */
        String MED_MEDICATION_DEFINITION = "med_medication_definition";

        /**
         * 药品信息
         */
        String MED_MEDICATION = "med_medication";

        /**
         * 器材定义
         */
        String ADM_DEVICE_DEFINITION = "adm_device_definition";

        /**
         * 器材信息
         */
        String ADM_DEVICE = "adm_device";

        /**
         * 活动定义
         */
        String WOR_ACTIVITY_DEFINITION = "wor_activity_definition";

        /**
         * 发放请求
         */
        String WOR_SUPPLY_REQUEST = "wor_supply_request";

        /**
         * 药品请求
         */
        String MED_MEDICATION_REQUEST = "med_medication_request";

        /**
         * 服务请求
         */
        String WOR_SERVICE_REQUEST = "wor_service_request";

        /**
         * 耗材请求
         */
        String WOR_DEVICE_REQUEST = "wor_device_request";
    }

    /**
     * 字段名常量
     */
    public interface FieldName {

        /**
         * 单据号
         */
        String SupplyBusNo = "supply_bus_no";

        /**
         * 患者院内编码
         */
        String PatientBusNo = "patient_bus_no";

        /**
         * 患者ID
         */
        String PatientId = "patient_id";

        /**
         * 就诊号
         */
        String EncounterBusNo = "encounter_bus_no";
        /**
         * 身份证号
         */
        String idCard = "id_card";

        /**
         * 拼音码
         */
        String PatientPyStr = "patient_py_str";

        /**
         * 五笔码
         */
        String PatientWbStr = "patient_wb_str";

        /**
         * 患者姓名
         */
        String PatientName = "patient_name";

        /**
         * 请求状态
         */
        String RequestStatus = "request_status";

        /**
         * 创建时间
         */
        String CreateTime = "create_time";

        /**
         * 请求基于什么的ID
         */
        String BasedOnId = "based_on_id";

        /**
         * 药品id
         */
        String MedicationId = "medication_id";

        /**
         * 就诊Id
         */
        String encouterId = "encouter_id";

        /**
         * 合同编码
         */
        String contractNo = "contract_no";

        /**
         * 支付流水
         */
        String paymentNo = "payment_no";
    }

    /**
     * 业务常量
     */
    public interface BusinessName {

        /**
         * 西医诊断
         */
        String WESTERN_MEDICINE_DIAGNOSIS = "西医诊断";

        /**
         * 中医诊断
         */
        String TCM_DIAGNOSIS = "中医诊断";
    }

    /**
     * 字典字段名常量
     */
    public interface DictName {
        /**
         * 器材
         */
        String MED_CATEGORY_CODE = "med_category_code";
        /**
         * 药品分类
         */
        String DEVICE_CATEGORY_CODE = "device_category_code";
        /**
         * 诊疗分类
         */
        String DIAGNOSIS_CATEGORY_CODE = "activity_category_code";

    }

    /**
     * sql条件常量
     */
    public interface SqlCondition {
        /**
         * 关于库存的表名str
         */
        String ABOUT_INVENTORY_TABLE_STR =
            "'med_medication_definition', 'adm_device_definition', 'wor_activity_definition'";
    }

}
