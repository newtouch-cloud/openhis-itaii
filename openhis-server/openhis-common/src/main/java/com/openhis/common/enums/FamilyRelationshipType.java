package com.openhis.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 家庭关系
 *
 * @author liuhr
 * @date 2025/2/25
 */
@Getter
@AllArgsConstructor
public enum FamilyRelationshipType implements HisEnumInterface {

    SELF(1, "01", "本人"),
    HOUSEHOLDER(2, "02", "户主"),
    SPOUSE(10, "10", "配偶"),
    HUSBAND(11, "11", "夫"),
    WIFE(12, "12", "妻"),
    SON(20, "20", "子"),
    ONLY_SON(21, "21", "独生子"),
    ELDEST_SON(22, "22", "长子"),
    SECOND_SON(23, "23", "次子"),
    THIRD_SON(24, "24", "三子"),
    FOURTH_SON(25, "25", "四子"),
    FIFTH_SON(26, "26", "五子"),
    ADOPTED_OR_STEPSON(27, "27", "养子或继子"),
    SON_IN_LAW(28, "28", "女婿"),
    OTHER_SON(29, "29", "其他儿子"),
    DAUGHTER(30, "30", "女"),
    ONLY_DAUGHTER(31, "31", "独生女"),
    ELDEST_DAUGHTER(32, "32", "长女"),
    SECOND_DAUGHTER(33, "33", "次女"),
    THIRD_DAUGHTER(34, "34", "三女"),
    FOURTH_DAUGHTER(35, "35", "四女"),
    FIFTH_DAUGHTER(36, "36", "五女"),
    ADOPTED_OR_STEPDAUGHTER(37, "37", "养女或继女"),
    DAUGHTER_IN_LAW(38, "38", "儿媳"),
    OTHER_DAUGHTER(39, "39", "其他女儿"),
    GRANDCHILD(40, "40", "孙子、孙女、外孙子、外孙女"),
    GRANDSON(41, "41", "孙子"),
    GRANDDAUGHTER(42, "42", "孙女"),
    GRANDSON_IN_LAW(43, "43", "外孙子"),
    GRANDDAUGHTER_IN_LAW(44, "44", "外孙女"),
    GRANDSONS_DAUGHTER_IN_LAW(45, "45", "孙媳妇或外孙媳妇"),
    GRANDDAUGHTERS_SON_IN_LAW(46, "46", "孙女婿或外孙女婿"),
    GREAT_GRANDSON(47, "47", "曾孙子或外曾孙子"),
    GREAT_GRANDDAUGHTER(48, "48", "曾孙女或外曾孙女"),
    OTHER_GRANDCHILD(49, "49", "其他孙子、孙女、外孙子、外孙女"),
    PARENTS(50, "50", "父母"),
    FATHER(51, "51", "父亲"),
    MOTHER(52, "52", "母亲"),
    FATHER_IN_LAW(53, "53", "公公"),
    MOTHER_IN_LAW(54, "54", "婆婆"),
    FATHER_IN_LAW_OF_SPOUSE(55, "55", "岳父"),
    MOTHER_IN_LAW_OF_SPOUSE(56, "56", "岳母"),
    STEP_OR_FOSTER_FATHER(57, "57", "继父或养父"),
    STEP_OR_FOSTER_MOTHER(58, "58", "继母或养母"),
    OTHER_PARENT(59, "59", "其他父母关系"),
    GRANDPARENTS(60, "60", "祖父母、外祖父母"),
    GRANDFATHER(61, "61", "祖父"),
    GRANDMOTHER(62, "62", "祖母"),
    GRANDFATHER_IN_LAW(63, "63", "外祖父"),
    GRANDMOTHER_IN_LAW(64, "64", "外祖母"),
    GRANDPARENTS_OF_SPOUSE(65, "65", "配偶的祖父母、外祖父母"),
    GREAT_GRANDFATHER(66, "66", "曾祖父"),
    GREAT_GRANDMOTHER(67, "67", "曾祖母"),
    GREAT_GRANDPARENTS_OF_SPOUSE(68, "68", "配偶的曾祖父母、外曾祖父母"),
    OTHER_GRANDPARENT(69, "69", "其他祖父母和外祖父母关系"),
    SIBLINGS(70, "70", "兄、弟、姐、妹"),
    BROTHER(71, "71", "兄"),
    SISTER_IN_LAW(72, "72", "嫂"),
    YOUNGER_BROTHER(73, "73", "弟"),
    YOUNGER_BROTHERS_WIFE(74, "74", "弟媳"),
    OLDER_SISTER(75, "75", "姐姐"),
    OLDER_SISTERS_HUSBAND(76, "76", "姐夫"),
    YOUNGER_SISTER(77, "77", "妹妹"),
    YOUNGER_SISTERS_HUSBAND(78, "78", "妹夫"),
    OTHER_SIBLINGS(79, "79", "其他兄弟姐妹"),
    OTHER_RELATIVES(80, "80", "其他"),
    UNCLE(81, "81", "伯父"),
    AUNT(82, "82", "伯母"),
    UNCLE_ON_FATHERS_SIDE(83, "83", "叔父"),
    AUNT_ON_FATHERS_SIDE(84, "84", "婶母"),
    UNCLE_ON_MOTHERS_SIDE(85, "85", "舅父"),
    AUNT_ON_MOTHERS_SIDE(86, "86", "舅母"),
    FOSTER_UNCLE(87, "87", "姨父"),
    FOSTER_AUNT(88, "88", "姨母"),
    FOSTER_UNCLE_ON_FATHERS_SIDE(89, "89", "姑父"),
    FOSTER_AUNT_ON_FATHERS_SIDE(90, "90", "姑母"),
    COUSINS(91, "91", "堂兄弟、堂姐妹"),
    COUSINS_ON_MOTHERS_SIDE(92, "92", "表兄弟、表姐妹"),
    NEPHEW(93, "93", "侄子"),
    NEPHEW_DAUGHTER(94, "94", "侄女"),
    NIECE(95, "95", "外甥"),
    NIECE_DAUGHTER(96, "96", "外甥女"),
    OTHER_RELATIVE(97, "97", "其他亲属"),
    NON_RELATIVE(99, "99", "非亲属");

    @EnumValue
    private final Integer value;
    private final String code;
    private final String info;

}