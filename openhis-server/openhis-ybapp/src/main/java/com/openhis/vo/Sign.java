package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.core.common.core.domain.HisBaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 *【9001】【9002】签到 签退
 *
 * @author SunJQ
 * @date 2025-04-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class Sign extends HisBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
	/**主键*/
	@TableId(type = IdType.ASSIGN_UUID)
    @JSONField(serialize=false)
    private String id;
	/**操作员编号*/
    @JSONField(name="opter_no")
    private String opterNo;
	/**签到编号*/
    @JSONField(name="sign_no")
    private String signNo;
	/**签到时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(serialize=false)
    private java.util.Date signTime;
	/**签退时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(serialize=false)
    private java.util.Date signOutTime;
	/**状态，0-新建，1-签到，2-签退*/
    @JSONField(serialize=false)
    private String status;
    /** 签到MAC地址 */
    @JSONField(name="mac")
    private String mac;
    /** 签到IP地址 */
    @JSONField(name="ip")
    private String ip;
//
//	/**租户ID*/
//    private String tenantId;
}
