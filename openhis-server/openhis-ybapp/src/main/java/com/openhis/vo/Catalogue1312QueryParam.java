package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

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
public class Catalogue1312QueryParam {

    // 医保目录编码
    @JSONField(name = "hilist_code")
    private String hilistCode;

    // 参保地区编码
    @JSONField(name = "insuplc_admdvs")
    private String insuplcAdmdvs;
    // 更新时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "updt_time")
    private Date updtTime;

    // 页数
    @JSONField(name = "page_num")
    private Integer pageNum;
    // 当前页面查询数量
    @JSONField(name = "page_size")
    private Integer pageSize;

    // 解密标记
    @JSONField(name = "decrypt_flag")
    private String decryptFlag;


}
