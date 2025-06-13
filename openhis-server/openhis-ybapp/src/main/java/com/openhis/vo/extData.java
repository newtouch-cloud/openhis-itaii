package com.openhis.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 【吉林省本地化】输入补充字段
 *
 * @author gaoyy
 * @date 2025-05-08
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class extData {
    // 1. 就医地分中心
    @JSONField(name = "mdtrtarea_subcent")
    private String mdtrtareaSubcent;

    // 2. 参保地分中心
    @JSONField(name = "insuplc_subcent")
    private String insuplcSubcent;

    // 3. 急诊核定流水号
    @JSONField(name = "er_crtf_evtsn")
    private String erCrtfEvtsn;

    // 4. 就诊科室
    @JSONField(name = "dept_name")
    private String deptName;

    // 5. 使用方法
    @JSONField(name = "used_mtd")
    private String usedMtd;

    // 6. 现病史
    @JSONField(name = "psndisehis")
    private String psndisehis;

    // 7. 生命体征
    @JSONField(name = "life_symptom")
    private String lifeSymptom;

    // 8. 临床病症
    @JSONField(name = "clnc_cond")
    private String clncCond;

    // 9. 门（急）诊诊断
    @JSONField(name = "disediag")
    private String disediag;

    // 10. 入院诊断
    @JSONField(name = "adm_disediag_name")
    private String admDisediagName;

    // 11. 判定依据
    @JSONField(name = "evid")
    private String evid;

    // 12. 首诊医生
    @JSONField(name = "fst_doctor")
    private String fstDoctor;

    // 13. 科主任
    @JSONField(name = "deptort")
    private String deptort;

    // 14. 就诊时间（入院时间）（表示：YYYYMMDDHH24MISS）
    @JSONField(name = "adm_date")
    private String admDate;

    // 15. 姓名
    @JSONField(name = "name")
    private String name;

    // 16. 身份证
    @JSONField(name = "certno")
    private String certno;

    // 17. 年龄
    @JSONField(name = "age")
    private String age;

    // 18. 性别（1：男，2：女）
    @JSONField(name = "gend")
    private String gend;
}
