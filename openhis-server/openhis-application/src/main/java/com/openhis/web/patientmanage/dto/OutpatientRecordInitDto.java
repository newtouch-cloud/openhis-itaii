package com.openhis.web.patientmanage.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 门诊记录初始化
 *
 * @author liuhr
 * @date 2025/3/17
 */
@Data
@Accessors(chain = true)
public class OutpatientRecordInitDto {

    //获取医生姓名列表
    private  List<String> doctorNames;

}
