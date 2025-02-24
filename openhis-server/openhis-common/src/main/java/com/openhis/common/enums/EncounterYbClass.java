package com.openhis.common.enums;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*代码值	代码名称  对应7.28医疗类别med_type 在2203接口用到此信息
        25	异地住院
        26	单病种住院
        27	自主就医
        81	意外伤害门诊
        15	特药
        28	日间手术
        61	照护保险
        11	普通门诊
        12	门诊挂号
        13	急诊
        14	门诊慢特病
        990404	门诊特检特治（限吉林市）
        21	普通住院
        22	外伤住院
        23	转外诊治住院
        24	急诊转住院
        41	定点药店购药
        51	生育门诊
        52	生育住院
        5212	生育新生儿费用
        16	中医特色门诊
        29	起付线治疗
        9107	体检
        3101	低自付住院
        3102	低自付门诊
        140104	门诊慢病
        140201	门诊特病
        2114	舒缓疗护住院*/
@Getter
@AllArgsConstructor
public enum EncounterYbClass {
    // 住院服务
    ORDINARY_HOSPITALIZATION(21, "21" , "普通住院"),
    FOREIGN_HOSPITALIZATION(25,"25", "异地住院"),
    SINGLE_DIAGNOSIS_HOSPITALIZATION(26,"26", "单病种住院"),
    INJURY_HOSPITALIZATION(22,"22", "外伤住院"),
    TRANSFER_HOSPITALIZATION(23,"23", "转外诊治住院"),
    EMERGENCY_TRANSFER_HOSPITALIZATION(24,"24", "急诊转住院"),
    PALLIATIVE_CARE_HOSPITALIZATION(2114,"2114", "舒缓疗护住院"),
    LOW_COPAY_HOSPITALIZATION(3101,"3101", "低自付住院"),

    // 门诊服务
    ORDINARY_OUTPATIENT(11,"11", "普通门诊"),
    OUTPATIENT_REGISTRATION(12,"12", "门诊挂号"),
    EMERGENCY(13,"13", "急诊"),
    CHRONIC_SPECIAL_OUTPATIENT(14,"14", "门诊慢特病"),
    SPECIAL_MEDICATION(15,"15", "特药"),
    TRADITIONAL_CHINESE_MEDICINE_OUTPATIENT(16,"16", "中医特色门诊"),
    OUTPATIENT_SPECIAL_EXAMINATION_TREATMENT(990404,"990404", "门诊特检特治（限吉林市）"),
    CHRONIC_DISEASE_OUTPATIENT(140104,"140104", "门诊慢病"),
    SPECIAL_DISEASE_OUTPATIENT(140201,"140201", "门诊特病"),
    LOW_COPAY_OUTPATIENT(3102,"3102", "低自付门诊"),

    // 其他服务
    SELF_MEDICATION(27,"27", "自主就医"),
    ACCIDENTAL_INJURY_OUTPATIENT(81,"81", "意外伤害门诊"),
    DAY_SURGERY(28,"28", "日间手术"),
    NURSING_INSURANCE(61,"61", "照护保险"),
    PHARMACY_PURCHASE(41,"41", "定点药店购药"),
    MATERNITY_OUTPATIENT(51,"51", "生育门诊"),
    MATERNITY_HOSPITALIZATION(52,"52", "生育住院"),
    MATERNITY_NEWBORN_COST(5212,"5212", "生育新生儿费用"),
    DEDUCTIBLE_TREATMENT(29,"29", "起付线治疗"),
    PHYSICAL_EXAMINATION(9107,"9107", "体检");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;
}
