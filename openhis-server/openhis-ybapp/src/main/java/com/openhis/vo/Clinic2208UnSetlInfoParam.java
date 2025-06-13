package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.core.common.core.domain.HisBaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
//    @JSONField(name="insuplc_admdvs")
//    private String insuplcAdmdvs;
	//人员编号
    @JSONField(name="psn_no")
    private String psnNo;

}
