/*
 * Copyright ©2023 CJB-CNIT Team. All rights reserved
 */
package com.openhis.yb.dto;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.Date;

/**
 * 【3401】科室信息上传
 *
 * @author SunJQ
 * @date 2025-04-28
 */
public class HospDept3401Param {

    // 1. 医院科室编码（院内唯一编码）
    @JSONField(name = "hosp_dept_codg") // JSON字段名映射
    private String hospDeptCodg;

    // 2. 科别（参照科室代码）
    @JSONField(name = "caty")
    private String caty;

    // 3. 医院科室名称
    @JSONField(name = "hosp_dept_name")
    private String hospDeptName;

    // 4. 开始时间（日期时间格式）
    @JSONField(name = "begntime", format = "yyyy-MM-dd HH:mm:ss") // 指定时间格式
    private Date begnTime;

    // 5. 结束时间（日期时间格式）
    @JSONField(name = "endtime", format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    // 6. 科室简介
    @JSONField(name = "itro")
    private String itro;

    // 7. 科室负责人姓名
    @JSONField(name = "dept_resper_name")
    private String deptResperName;

    // 8. 科室负责人电话
    @JSONField(name = "dept_resper_tel")
    private String deptResperTel;

    // 9. 科室医疗服务范围
    @JSONField(name = "dept_med_serv_scp")
    private String deptMedServScp;

    // 10. 科室成立日期（日期格式）
    @JSONField(name = "dept_estbdat", format = "yyyy-MM-dd")
    private Date deptEstbdat;

    // 11. 批准床位数量（长整型）
    @JSONField(name = "aprv_bed_cnt")
    private Long aprvBedCnt;

    // 12. 医保认可床位数（长整型）
    @JSONField(name = "hi_crtf_bed_cnt")
    private Long hiCrtfBedCnt;

    // 13. 统筹区编号
    @JSONField(name = "poolarea_no")
    private String poolareaNo;

    // 14. 医师人数（整型）
    @JSONField(name = "dr_psncnt")
    private Integer drPsncnt;

    // 15. 药师人数（整型）
    @JSONField(name = "phar_psncnt")
    private Integer pharPsncnt;

    // 16. 护士人数（整型）
    @JSONField(name = "nurs_psncnt")
    private Integer nursPsncnt;

    // 17. 技师人数（整型）
    @JSONField(name = "tecn_psncnt")
    private Integer tecnPsncnt;

    // 18. 备注
    @JSONField(name = "memo")
    private String memo;
}
