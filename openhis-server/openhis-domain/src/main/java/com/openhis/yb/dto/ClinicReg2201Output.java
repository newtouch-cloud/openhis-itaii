package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Accessors(chain = true)
public class ClinicReg2201Output {
    private static final long serialVersionUID = 1L;
    //主键
    @TableId(type = IdType.ASSIGN_ID)
    @JSONField(serialize=false)
    private String id;
    //就诊ID
    private String mdtrtId;
    //参保地医保区划
    @JSONField(serialize=false)
    private String insuplcAdmdvs;
    //人员编号
    private String psnNo;
    //险种类型
    private String insutype;
    //医疗类别
    @JSONField(serialize=false)
    private String medType;
    //挂号时间
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date begntime;
    //就诊凭证类型
    private String mdtrtCertType;
    //就诊凭证编号
    private String mdtrtCertNo;
    //证件加密串
    @JSONField(serialize=false)
    @TableField(exist = false)
    private String mdtrtCertNoEncrypt;
    //住院/门诊号
    private String iptOtpNo;
    //医师编码
    private String atddrNo;
    //医师姓名
    private String drName;
    //科室编码
    private String deptCode;
    //科室名称
    private String deptName;
    //科别
    private String caty;
    //挂号费
    private Double regFee;
    //检查费
    private Double checkFee;
    //应收金额
    private Double totalFee;
    //状态,0-挂号,1-看诊，2-费用明细，3-预结算，4-结算
    private String status;
}
