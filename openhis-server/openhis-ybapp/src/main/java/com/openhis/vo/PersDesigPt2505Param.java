package com.openhis.vo;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 【2505】人员定点备案（输入）
 *
 * @author gaoyy
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class PersDesigPt2505Param {
    // 1. 人员编号
    @JSONField(name = "psn_no")
    private String psnNo;

    // 2. 联系电话
    @JSONField(name = "tel")
    private String tel;

    // 3. 联系地址
    @JSONField(name = "addr")
    private String addr;

    // 4. 业务申请类型
    @JSONField(name = "biz_appy_type")
    private String bizAppyType;

    // 5. 开始日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "begndate")
    private Date begndate;

    // 6. 结束日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "enddate")
    private Date enddate;

    // 7. 代办人姓名
    @JSONField(name = "agnter_name")
    private String agnterName;

    // 8. 代办人证件类型
    @JSONField(name = "agnter_cert_type")
    private String agnterCertType;

    // 9. 代办人证件号码
    @JSONField(name = "agnter_certno")
    private String agnterCertno;

    // 10. 代办人联系方式
    @JSONField(name = "agnter_tel")
    private String agnterTel;

    // 11. 代办人联系地址
    @JSONField(name = "agnter_addr")
    private String agnterAddr;

    // 12. 代办人关系
    @JSONField(name = "agnter_rlts")
    private String agnterRlts;

    // 13. 定点排序号
    @JSONField(name = "fix_srt_no")
    private String fixSrtNo;

    // 14. 定点医药机构编号
    @JSONField(name = "fixmedins_code")
    private String fixmedinsCode;

    // 15. 定点医药机构名称
    @JSONField(name = "fixmedins_name")
    private String fixmedinsName;

    // 16. 备注
    @JSONField(name = "memo")
    private String memo;

    // 17. 扩展字段
    @JSONField(name = "extData")
    private JSONObject extData;

    // 18. 待遇申报明细流水号（输出）
    @JSONField(name = "trt_dcla_detl_sn")
    private String trtDclaDetlSn;
}
