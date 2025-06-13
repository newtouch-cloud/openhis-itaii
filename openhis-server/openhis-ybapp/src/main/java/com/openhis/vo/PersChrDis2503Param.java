package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 【2503】人员慢特病备案（输入）
 *
 * @author gaoyy
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PersChrDis2503Param {
    // 1. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 2. 险种类型
    @JSONField(name = "insutype")
    private String insutype;

    // 3. 门慢门特病种目录代码
    @JSONField(name = "opsp_dise_code")
    private String opspDiseCode;

    // 4. 门慢门特病种名称
    @JSONField(name = "opsp_dise_name")
    private String opspDiseName;

    // 5. 联系电话
    @JSONField(name = "tel")
    private String tel;

    // 6. 联系地址
    @JSONField(name = "addr")
    private String addr;

    // 7. 参保机构医保区划
    @JSONField(name = "insu_optins")
    private String insuOptins;

    // 8. 鉴定定点医药机构编号
    @JSONField(name = "ide_fixmedins_no")
    private String ideFixmedinsNo;

    // 9. 鉴定定点医药机构名称
    @JSONField(name = "ide_fixmedins_name")
    private String ideFixmedinsName;

    // 10. 医院鉴定日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "hosp_ide_date")
    private Date hospIdeDate;

    // 11. 诊断医师编码
    @JSONField(name = "diag_dr_codg")
    private String diagDrCodg;

    // 12. 诊断医师姓名
    @JSONField(name = "diag_dr_name")
    private String diagDrName;

    // 13. 开始日期
    @JSONField(name = "begndate", format = "yyyy-MM-dd")
    private Date begndate;

    // 15. 结束日期
    @JSONField(name = "enddate", format = "yyyy-MM-dd")
    private Date enddate;

    // 16. 待遇申报明细流水号（输出）
    @JSONField(name = "trt_dcla_detl_sn")
    private String trtDclaDetlSn;
}
