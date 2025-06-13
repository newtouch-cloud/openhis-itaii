package com.openhis.ybcatalog.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@TableName("yb_catalog_special_insurance_disease")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CatalogSpecialInsuranceDisease {

    private String diseaseCode; // 门慢门特病种目录代码
    private String diseaseCategoryName; // 门慢门特病种大类名称
    private String diseaseSubcategoryName; // 门慢门特病种细分类名称
    private String medicalInsuranceZone; // 医保区划
    private String remark; // 备注
    private String validFlag; // 有效标志
    private String uniqueRecordId; // 唯一记录号
    private String dataCreateTime; // 数据创建时间
    private String dataUpdateTime; // 数据更新时间
    private String versionNumber; // 版本号
    private String diseaseDescription; // 病种内涵
    private String versionName; // 版本名称
    private String guidePageNumber; // 诊疗指南页码
    private String guideElectronicFile; // 诊疗指南电子档案
    private String diseaseName; // 门慢门特病种名称
    private String diseaseCategoryCode; // 门慢门特病种大类代码
}
