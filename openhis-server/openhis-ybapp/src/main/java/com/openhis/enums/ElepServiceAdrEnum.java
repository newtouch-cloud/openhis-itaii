package com.openhis.enums;

/**
 * 接口地址
 */
public enum ElepServiceAdrEnum {

    /** 电子处方上传预核验 */
    A0001("A0001", "/fixmedins/uploadChk", "【A0001】电子处方上传预核验"),
    /** 电子处方医保电子签名*/
    A0002("A0002", "/fixmedins/rxFixmedinsSign", "【A0002】电子处方医保电子签名"),
    /** 电子处方上传 */
    A0003("A0003", "/fixmedins/rxFileUpld", "【A0003】电子处方上传"),
    /** 电子处方撤销 */
    A0004("A0004", "/fixmedins/rxUndo", "【A0004】电子处方撤销"),
    /** 电子处方信息查询 */
    A0005("A0005", "/fixmedins/hospRxDetlQuery", "【A0005】电子处方信息查询"),
    /** 电子处方取药结果查询 */
    A0006("A0006", "/fixmedins/rxSetlInfoQuery", "【A0006】电子处方取药结果查询");


    /** 接口编号 */
    private final String num;
    /** 接口地址 */
    private final String address;
    /** 接口描述 */
    private final String description;

    ElepServiceAdrEnum(String num, String address, String description) {
        this.num = num;
        this.address = address;
        this.description = description;
    }

    public String getNum() {
        return num;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

}
