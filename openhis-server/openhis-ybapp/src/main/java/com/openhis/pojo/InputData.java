package com.openhis.pojo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.openhis.constant.InputDataTypeConst;

import lombok.Data;

/**
 * 医保入参
 */
@Data
public class InputData<T, H> {

    @JSONField(serialize = false)
    private String insuplcAdmdvs;
    @JSONField(name = InputDataTypeConst.DATA)
    private T data;
    @JSONField(name = InputDataTypeConst.FEE_DETAIL)
    private T feeDetail;
    @JSONField(name = InputDataTypeConst.SIGN_IN)
    private T signIn;
    @JSONField(name = InputDataTypeConst.SIGN_OUT)
    private T signOut;
    @JSONField(name = InputDataTypeConst.MD_TRT_INFO)
    private T mdTrtInfo;
    @JSONField(name = InputDataTypeConst.DI_SE_INFO)
    private H diSeInfo;
    @JSONField(name = InputDataTypeConst.DRUG_INFO)
    private T druginfo;
    @JSONField(name = InputDataTypeConst.DRUG_DETAIL)
    private H drugdetail;
    @JSONField(name = InputDataTypeConst.DSCG_INFO)
    private T dscginfo;
    @JSONField(name = InputDataTypeConst.PURC_INFO)
    private T purcinfo;
    @JSONField(name = InputDataTypeConst.INV_INFO)
    private T invinfo;
    @JSONField(name = InputDataTypeConst.SEL_INFO)
    private T selinfo;

    public InputData(T param, String insuplcAdmdvs, String flag) {
        this.insuplcAdmdvs = insuplcAdmdvs == null ? "" : insuplcAdmdvs;
        if (InputDataTypeConst.DATA.equals(flag)) {
            this.data = param;
        } else if (InputDataTypeConst.FEE_DETAIL.equals(flag)) {
            this.feeDetail = param;
        } else if (InputDataTypeConst.SIGN_IN.equals(flag)) {
            this.signIn = param;
        } else if (InputDataTypeConst.SIGN_OUT.equals(flag)) {
            this.signOut = param;
        } else if (InputDataTypeConst.PURC_INFO.equals(flag)) {
            this.purcinfo = param;
        } else if (InputDataTypeConst.INV_INFO.equals(flag)) {
            this.invinfo = param;
        } else if (InputDataTypeConst.SEL_INFO.equals(flag)) {
            this.selinfo = param;
        }
    }

    public InputData(T param1, H param2, String insuplcAdmdvs, String flag) {
        this.insuplcAdmdvs = insuplcAdmdvs == null ? "" : insuplcAdmdvs;
        if (InputDataTypeConst.MD_TRT_INFO.equals(flag)) {
            this.mdTrtInfo = param1;
            this.diSeInfo = param2;
        } else if (InputDataTypeConst.DRUG_INFO.equals(flag)) {
            this.druginfo = param1;
            this.drugdetail = param2;
        } else if (InputDataTypeConst.DSCG_INFO.equals(flag)) {
            this.dscginfo = param1;
            this.diSeInfo = param2;
        }
    }

}
