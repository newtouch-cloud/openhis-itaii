package com.openhis.web.doctorstation.dto;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 更新组号参数类
 */
@Data
@Accessors(chain = true)
public class UpdateGroupIdParam {

    /**
     * 保存医嘱 dto
     */
    private List<UpdateGroupDto> groupList;

}
