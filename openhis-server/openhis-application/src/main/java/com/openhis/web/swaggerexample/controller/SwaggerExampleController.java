package com.openhis.web.swaggerexample.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.core.common.core.domain.R;
import com.openhis.web.swaggerexample.dto.GetExampleResult;
import com.openhis.web.swaggerexample.dto.PostExampleParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * swagger例子Controller
 *
 * @author guorui
 * @date 2025/3/17
 */
@RestController
@Slf4j
@AllArgsConstructor
@Api(tags = "swagger例子")
@RequestMapping("/swagger-example")
public class SwaggerExampleController {
    // TODO:这只是个例子，后期请删除

    /**
     * get接口示例
     * 
     * @param param1 路径参数
     * @param param2 一般参数
     * @return 结果
     */
    @ApiOperation(value = "get接口示例")
    @GetMapping("/get/{param1}")
    public R<List<GetExampleResult>> getExample(@PathVariable @ApiParam(name = "param1", value = "路径参数") String param1,
        @RequestParam @ApiParam(name = "param2", value = "一般参数") String param2) {
        return R.ok(Collections.emptyList(), "查询成功！");
    }

    /**
     * post接口示例
     * 
     * @param postExampleParam POST请求参数类
     * @return 结果
     */
    @ApiOperation(value = "post接口示例")
    @PostMapping("/post")
    public R<?> postExample(@RequestBody PostExampleParam postExampleParam) {
        return R.ok("处理成功！");
    }

}
