package com.openhis.yb.dto;

import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 【1312】医保目录信息查询
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Catalogue1312Output extends HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    // 医保目录编码
    private String hilistCode;

    // 医保目录名称
    private String hilistName;

    // 参保机构医保区划
    private String insuAdmdvs;

    // 开始日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date begndate;

    // 结束日期
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enddate;

    // 医疗收费项目类别
    private String medChrgitmType;

    // 收费项目等级
    private String chrgitmLv;

    // 限制使用标志
    private String lmtUsedFlag;

    // 目录类别
    private String listType;

    // 医疗使用标志
    private String medUseFlag;

    // 生育使用标志
    private String matnUsedFlag;

    // 医保目录使用类别
    private String hilistUseType;

    // 限复方使用类型
    private String lmtCpndType;

    // 五笔助记码
    private String wubi;

    // 拼音助记码
    private String pinyin;

    // 备注
    private String memo;

    // 有效标志
    private String valiFlag;

    // 唯一记录号
    private String rid;

    // 更新时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updtTime;

    // 创建人
    private String crterId;

    // 创建人姓名
    private String crterName;

    // 创建时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date crteTime;

    // 创建机构
    private String crteOptinsNo;

    // 经办人
    private String opterId;

    // 经办人姓名
    private String opterName;

    // 经办时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date optTime;

    // 经办机构
    private String optinsNo;

    // 统筹区
    private String poolareaNo;
}
