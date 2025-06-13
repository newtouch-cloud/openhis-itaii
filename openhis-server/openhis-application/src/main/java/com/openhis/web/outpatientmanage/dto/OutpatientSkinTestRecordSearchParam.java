package com.openhis.web.outpatientmanage.dto;

import lombok.Data;

/**
 * 门诊皮试记录查询体体条件类
 *
 * @author liuhr
 * @date 2025/3/5
 */
@Data
public class OutpatientSkinTestRecordSearchParam {

    /** 手机号 */
    private String phone;

    /** 皮试项目检查状态 */
    private Integer clinicalStatusEnum;

}
