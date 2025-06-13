package com.openhis.yb.dto;

import java.io.Serializable;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.core.common.annotation.Excel;
import com.core.common.core.domain.HisBaseEntity;
import com.openhis.common.annotation.Dict;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 *【2206】门诊预结算-输出参数
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
public class Clinic2206SetldetaiResult extends HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
	//主键
	@TableId(type = IdType.ASSIGN_ID)
    @JSONField(serialize=false)
    private String id;
	//结算清单主键
	@Excel(name = "结算清单主键", width = 15)
    @JSONField(serialize=false)
    private String orderId;
	//结算信息主键
    @JSONField(serialize=false)
    private String setlinfoId;
	//内部就诊ID
    @JSONField(serialize=false)
    private String regId;
	//基金支付类型
    @JSONField(name="fund_pay_type")
    @Dict(dictCode = "fund_pay_type")
    private String fundPayType;
	//符合政策范围金额
    @JSONField(name="inscp_scp_amt")
    private Double inscpScpAmt;
	//本次可支付限额金额
    @JSONField(name="crt_payb_lmt_amt")
    private Double crtPaybLmtAmt;
	//基金支付金额
    @JSONField(name="fund_payamt")
    private Double fundPayamt;
	//基金支付类型名称
    @JSONField(name="fund_pay_type_name")
    private String fundPayTypeName;
	//结算过程信息
    @JSONField(name="setl_proc_info")
    private String setlProcInfo;
}
