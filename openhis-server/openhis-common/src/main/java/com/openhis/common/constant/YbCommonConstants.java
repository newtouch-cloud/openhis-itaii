/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.common.constant;

/**
 * 常量
 *
 * @author SunJQ
 * @date 2025-04-09
 */
public class YbCommonConstants {
    /**
     * 常量
     */
    public interface constants{
        /**
         * 医保目录字段
         */
        String END_TIME = "2035-12-01";
        /**
         * 医保目录字段
         */
        String YYYY_MM_DD = "yyyy-MM-dd";

        /** 标识字段 */
        String INFCODE = "infcode";

        /** 成功 */
        String SUCCESS = "0";
    }
    /**
     * 字段名常量
     */
    public interface FieldName {
        /**
         * 医保目录字段
         */
        String SyndromeTypeCode = "syndrome_type_code";
        /**
         * 医保目录字段
         */
        String SyndromeTypeName = "syndrome_type_name";
        /**
         * 医保目录字段
         */
        String UniqueRecordId = "unique_record_id";
        /**
         * 医保目录字段
         */
        String DiseaseCategoryCode = "disease_category_code";
        /**
         * 医保目录字段
         */
        String DiseaseCategoryName = "disease_category_name";
        /**
         * 医保目录字段
         */
        String CategoryName = "category_name";
        /**
         * 医保目录字段
         */
        String SubcategoryName = "subcategory_name";
        /**
         * 医保目录字段
         */
        String ItemName = "item_name";
        /**
         * 医保目录字段
         */
        String OperationName = "operation_name";
        /**
         * 医保目录字段
         */
        String OperationCode = "operation_code";
        /**
         * 医保目录字段
         */
        String MedicalCatalogCode = "medical_catalog_code";
        /**
         * 医保目录字段
         */
        String ConsumableCategory = "consumable_category";
        /**
         * 医保目录字段
         */
        String MaterialType = "material_type";
        /**
         * 医保目录字段
         */
        String Specification = "specification";
        /**
         * 医保目录字段
         */
        String MedicalServiceName = "medical_service_name";
        /**
         * 医保目录字段
         */
        String RegisteredName = "registered_name";
        /**
         * 医保目录字段
         */
        String ApprovalNo = "approval_no";
        /**
         * 医保目录字段
         */
        String ChapterName = "chapter_name";
        /**
         * 医保目录字段
         */
        String SectionName = "section_name";
        /**
         * 医保目录字段
         */
        String ConsumableName = "consumable_name";
        /**
         * 只保留每组中 版本号 最大的记录
         */
        String RowNumMax = "row_num_max";
    }

    /**
     * 门诊状态常量
     */
    public interface ClincStatusConst {
        String STATUS_0 = "0";
        String STATUS_1 = "1";
        String STATUS_2 = "2";
        String CANCLE = "-1";
    }

    public interface sqlConst{
        String LIMIT1 = "limit 1";
    }
}
