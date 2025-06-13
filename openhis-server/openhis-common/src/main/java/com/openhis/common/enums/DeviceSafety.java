package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeviceSafety {

    Magnetic_Resonance_Conditional(1, "C106046",
        "在MR环境中已证明安全的设备，在定义条件下。至少应解决静态磁场、切换梯度磁场和射频场的条件。可能需要包括物品特定配置在内的其他条件。参见ASTM F2503"),

    Magnetic_Resonance_Safe(2, "C106045", "该设备在暴露于任何MR环境时不会造成已知危害。MR安全物品由非导电、非金属和非磁性材料组成。请参阅ASTM F2503"),

    Magnetic_Resonance_Unsafe(3, "C106047", "对患者、医务人员或MR环境内其他人员构成不可接受风险的设备。参见ASTM F2503"),

    LABELING_DOES_NOT_CONTAIN_MRI_SAFETY_INFORMATION(4, "C113844", "标签者未在标签中提供使用设备在磁共振（MR）环境中的安全信息"),

    LABELED_AS_CONTAINING_NATURAL_RUBBER_LATEX(5, "C101673", "该设备标签表明天然橡胶乳胶是其成分之一。"),

    NOT_MADE_WITH_NATURAL_RUBBER_LATEX(6, "C106038", "该产品的材料组成不含有天然橡胶乳胶。");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

    public static DeviceSafety getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (DeviceSafety val : values()) {
            if (val.getValue().equals(value)) {
                return val;
            }
        }
        return null;
    }
}
