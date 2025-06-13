package com.openhis.yb.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *【2208】门诊结算撤销
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
public class Clinic2208UnSetlInfoParam extends HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

	//就诊ID
    @JSONField(name="mdtrt_id")
    private String mdtrtId;
	//结算ID
    @JSONField(name="setl_id")
    private String setlId;
	//参保地医保区划
    @JSONField(name="insuplc_admdvs")
    private String insuplcAdmdvs;
	//人员编号
    @JSONField(name="psn_no")
    private String psnNo;

}
