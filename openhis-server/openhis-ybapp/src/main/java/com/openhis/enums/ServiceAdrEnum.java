package com.openhis.enums;

/**
 * 接口地址
 */
public enum ServiceAdrEnum {

    /** 前置接口 */
    TOKEN("token", "/auth/oauth/token", "获取token"),
    P9001("9001", "/mbs/fmi/fsi/api/callFsiService/callService", "【9001】签到"),
    P9002("9002", "/mbs/fmi/fsi/api/callFsiService/callService", "【9002】签退"),
    P1101("1101", "/mbs/fmi/fsi/api/callFsiService/callService", "【1101】人员信息获取"),
    P1201("1201", "/mbs/fmi/fsi/api/callFsiService/callService", "【1201】医药机构信息获取"),
    P3301("3301", "/mbs/fmi/fsi/api/callFsiService/callService", "【3301】目录对照上传"),
    P3302("3302", "/mbs/fmi/fsi/api/callFsiService/callService", "【3302】目录对照撤销"),
    /** 目录下载接口 */
    D1301("1301","/mbs/fmi/fsi/api/callFsiService/callService","【1301】西药中成药目录下载"),
    D1302("1302","/mbs/fmi/fsi/api/callFsiService/callService","【1302】中药饮片目录下载"),
    D1303("1303","/mbs/fmi/fsi/api/callFsiService/callService","【1303】医疗机构制剂目录下载"),
    D1304("1304","/mbs/fmi/fsi/api/callFsiService/callService","【1304】民族药品目录查询"),
    D1305("1305","/mbs/fmi/fsi/api/callFsiService/callService","【1305】医疗服务项目目录下载"),
    D1306("1306","/mbs/fmi/fsi/api/callFsiService/callService","【1306】医用耗材目录下载"),
    D1307("1307","/mbs/fmi/fsi/api/callFsiService/callService","【1307】疾病与诊断目录下载"),
    D1308("1308","/mbs/fmi/fsi/api/callFsiService/callService","【1308】手术操作目录下载"),
    D1309("1309","/mbs/fmi/fsi/api/callFsiService/callService","【1309】门诊慢特病种目录下载"),
    D1310("1310","/mbs/fmi/fsi/api/callFsiService/callService","【1310】按病种付费病种目录下载"),
    D1311("1311","/mbs/fmi/fsi/api/callFsiService/callService","【1311】日间手术治疗病种目录下载"),
    D1312("1312","/mbs/fmi/fsi/api/callFsiService/callService","【1312】医保目录信息查询"),
    D1313("1313","/mbs/fmi/fsi/api/callFsiService/callService","【1313】肿瘤形态学目录下载"),
    D1314("1314","/mbs/fmi/fsi/api/callFsiService/callService","【1314】中医疾病目录下载"),
    D1315("1315","/mbs/fmi/fsi/api/callFsiService/callService","【1315】中医证候目录下载"),
    D1316("1316","/mbs/fmi/fsi/api/callFsiService/callService","【1316】医疗目录与医保目录匹配信息查询"),
    D1317("1317","/mbs/fmi/fsi/api/callFsiService/callService","【1317】医药机构目录匹配信息查询"),
    D1318("1318","/mbs/fmi/fsi/api/callFsiService/callService","【1318】医保目录限价信息查询"),
    D1319("1319","/mbs/fmi/fsi/api/callFsiService/callService","【1319】医保目录先自付比例信息查询"),
    D1320("1320","/mbs/fmi/fsi/api/callFsiService/callService","【1320】中药配方颗粒目录下载"),
    D1321("1321","/mbs/fmi/fsi/api/callFsiService/callService","【1321】医疗服务项目（新）目录下载"),
    /** 人员备案接口 */
    F2501("2501", "/mbs/fmi/fsi/api/callFsiService/callService", "【2501】转院备案"),
    F2502("2502", "/mbs/fmi/fsi/api/callFsiService/callService", "【2502】转院备案撤销"),
    F2503("2503", "/mbs/fmi/fsi/api/callFsiService/callService", "【2503】人员慢特病备案"),
    F2504("2504", "/mbs/fmi/fsi/api/callFsiService/callService", "【2504】人员慢特病备案撤销"),
    F2505("2505", "/mbs/fmi/fsi/api/callFsiService/callService", "【2505】人员定点备案"),
    F2506("2506", "/mbs/fmi/fsi/api/callFsiService/callService", "【2506】人员定点备案撤销"),
    /** 药店接口 */
    D2101("2101", "/mbs/fmi/fsi/api/callFsiService/callService", "【2101】药店预结算"),
    D2102("2102", "/mbs/fmi/fsi/api/callFsiService/callService", "【2102】药店结算"),
    D2103("2103", "/mbs/fmi/fsi/api/callFsiService/callService", "【2103】药店结算撤销"),
    /** 门诊接口 */
    C2201("2201", "/mbs/fmi/fsi/api/callFsiService/callService", "【2201】门诊挂号"),
    C2202("2202", "/mbs/fmi/fsi/api/callFsiService/callService", "【2202】门诊挂号撤销"),
    C2203("2203", "/mbs/fmi/fsi/api/callFsiService/callService", "【2203】门诊就诊信息上传"),
    C2203A("2203A", "/mbs/fmi/fsi/api/callFsiService/callService", "【2203A】门诊就诊信息上传"),
    C2204("2204", "/mbs/fmi/fsi/api/callFsiService/callService", "【2204】门诊费用明细上传"),
    C2205("2205", "/mbs/fmi/fsi/api/callFsiService/callService", "【2205】门诊费用明细撤销"),
    C2206("2206", "/mbs/fmi/fsi/api/callFsiService/callService", "【2206】门诊预结算"),
    C2207("2207", "/mbs/fmi/fsi/api/callFsiService/callService", "【2207】门诊结算"),
    C2208("2208", "/mbs/fmi/fsi/api/callFsiService/callService", "【2208】门诊费用撤销"),
    /** 住院接口 */
    H2401("2401", "/mbs/fmi/fsi/api/callFsiService/callService", "【2401】入院办理"),
    H2402("2402", "/mbs/fmi/fsi/api/callFsiService/callService", "【2402】出院办理"),
    H2405("2405", "/mbs/fmi/fsi/api/callFsiService/callService", "【2405】出院撤销"),
    H2301("2301", "/mbs/fmi/fsi/api/callFsiService/callService", "【2301】住院费用明细上传"),
    H2302("2302", "/mbs/fmi/fsi/api/callFsiService/callService", "【2302】住院费用明细撤销"),
    H2303("2303", "/mbs/fmi/fsi/api/callFsiService/callService", "【2303】住院预结算"),
    H2304("2304", "/mbs/fmi/fsi/api/callFsiService/callService", "【2304】住院结算"),
    H2305("2305", "/mbs/fmi/fsi/api/callFsiService/callService", "【2305】住院结算撤销"),
    H2404("2404", "/mbs/fmi/fsi/api/callFsiService/callService", "【2404】入院撤销"),
    /** 医药机构费用结算业务 */
    R3201("3201", "/mbs/fmi/fsi/api/callFsiService/callService", "【3201】医药机构费用结算对总账"),
    R3202("3202", "/mbs/fmi/fsi/api/callFsiService/callService", "【3202】医药机构费用结算对明细账"),
    R3203("3203", "/mbs/fmi/fsi/api/callFsiService/callService", "【3203】清算申请"),
    R3203A("3203A", "/mbs/fmi/fsi/api/callFsiService/callService", "【3203A】清算申请(吉林省)"),
    R3204("3204", "/mbs/fmi/fsi/api/callFsiService/callService", "【3204】清算申请撤销"),
    R3204A("3204A", "/mbs/fmi/fsi/api/callFsiService/callService", "【3204A】清算申请撤销(吉林省)"),
    R3205A("3205A", "/mbs/fmi/fsi/api/callFsiService/callService", "【3205A】清算申请状态查询（吉林省）"),
    R3206A("3206A", "/mbs/fmi/fsi/api/callFsiService/callService", "【3206A】清算机构查询(吉林省)"),
    R3209A("3206A", "/mbs/fmi/fsi/api/callFsiService/callService", "【3209A】查询跨省三方对账未成功数据(吉林省)"),
    R13203("13203", "/mbs/fmi/fsi/api/callFsiService/callService", "【13203】医药机构费用结算日对账结果查询"),
    /** 进销存业务 */
    I3501("3501", "/mbs/fmi/fsi/api/callFsiService/callService", "【3501】商品盘存上传"),
    I3502("3502", "/mbs/fmi/fsi/api/callFsiService/callService", "【3502】商品库存变更"),
    I3503("3503", "/mbs/fmi/fsi/api/callFsiService/callService", "【3503】商品采购"),
    I3504("3504", "/mbs/fmi/fsi/api/callFsiService/callService", "【3504】商品采购退货"),
    I3505("3505", "/mbs/fmi/fsi/api/callFsiService/callService", "【3505】商品销售"),
    I3506("3506", "/mbs/fmi/fsi/api/callFsiService/callService", "【3506】商品销售退货"),
    I3507("3507", "/mbs/fmi/fsi/api/callFsiService/callService", "【3507】商品信息删除"),
    /** 医保服务查询业务 */
    G5301("5301", "/mbs/fmi/fsi/api/callFsiService/callService", "【5301】人员慢特病备案查询"),
    /** 文件上传下载 */
    U9101("9101", "/mbs/fmi/fsi/api/callFsiService/callService", "【9101】文件上传"),
    U9102("9102", "/mbs/fmi/fsi/api/callFsiService/callService", "【9102】文件下载");
    /** 接口编号 */
    private final String num;
    /** 接口地址 */
    private final String address;
    /** 接口描述 */
    private final String description;

    ServiceAdrEnum(String num, String address, String description) {
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

    public static ServiceAdrEnum getEnum(String num){
        for (ServiceAdrEnum serviceAdrEnum : ServiceAdrEnum.values()) {
            if (serviceAdrEnum.getNum().equals(num)){
                return serviceAdrEnum;
            }
        }
        return null;
    }
}
